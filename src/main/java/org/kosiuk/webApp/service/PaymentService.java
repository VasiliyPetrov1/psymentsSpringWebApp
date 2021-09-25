package org.kosiuk.webApp.service;

import org.kosiuk.webApp.dto.*;
import org.kosiuk.webApp.entity.*;
import org.kosiuk.webApp.exceptions.*;
import org.kosiuk.webApp.repository.PaymentRepository;
import org.kosiuk.webApp.util.concurrent.MoneyAccountMonitor;
import org.kosiuk.webApp.util.sumConversion.MoneyIntDecToStringAdapter;
import org.kosiuk.webApp.util.sumConversion.MoneyStringToIntDecAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.*;

@Service
public class PaymentService {

    @Value("${application.comissionPercentage}")
    private double comissionPercentage;
    private boolean paymentNumSetupPermission;
    private boolean isPaymentDtoOnAdding;
    private final PaymentRepository paymentRepository;
    private final CreditCardService creditCardService;
    private final MoneyAccountService moneyAccountService;
    private final AdditionalPropertiesService addPropService;
    private final TransactionService transactionService;
    private final Queue<PaymentSendingDto> paymentsOnNumberSetUpQueue;
    @Value("${application.paymentsPageSize}")
    private int pageSize;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, CreditCardService creditCardService,
                          MoneyAccountService moneyAccountService, AdditionalPropertiesService addPropService,
                          TransactionService transactionService) {
        this.paymentRepository = paymentRepository;
        this.creditCardService = creditCardService;
        this.moneyAccountService = moneyAccountService;
        this.addPropService = addPropService;
        this.transactionService = transactionService;
        this.paymentNumSetupPermission = true;
        this.paymentsOnNumberSetUpQueue = new ArrayDeque<>();
        this.isPaymentDtoOnAdding = false;
    }

    public List<PaymentDto> getAllPaymentDtos() {

        List<Payment> allPayments = (List<Payment>) paymentRepository.findAll();
        return allPayments.stream().map(payment -> convertPaymentToDto(payment)).collect(Collectors.toList());
    }

    public Page<Payment> getAllPaymentsPage(int pageNumber, String sortParameter) {
        Pageable pageable;
        if (sortParameter.equals("none")) {
            pageable = PageRequest.of(pageNumber - 1, pageSize);
        } else if (sortParameter.equals("number")){
            pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, "paymentId");
        } else if (sortParameter.equals("timeAsc")) {
            pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, "time");
        } else if (sortParameter.equals("timeDesc")) {
            pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.DESC, "time");
        } else {
            pageable = PageRequest.of(pageNumber - 1, pageSize);
        }
        return paymentRepository.findAll(pageable);
    }

    public List<PaymentDto> getAllSentByAccountPaymentDtos(Integer moneyAccId) {
        MoneyAccount moneyAccount = moneyAccountService.getMoneyAccountById(moneyAccId);
        List<Payment> allPayments = moneyAccount.getPayments();
        return allPayments.stream().map(payment -> convertPaymentToDto(payment)).collect(Collectors.toList());
    }

    public List<PaymentDto> getAllReceivedByAccountPaymentDtos(Integer moneyAccId) {
        MoneyAccount moneyAccount = moneyAccountService.getMoneyAccountById(moneyAccId);
        List<Transaction> allReceivedTransactions= moneyAccount.getReceivedTransactions();
        return allReceivedTransactions.stream().map(transaction -> convertPaymentToDto(transaction.getPayment())).
                collect(Collectors.toList());
    }

    public List<PaymentDto> getAllSortedPaymentDtos(List<PaymentDto> allPaymentDtos, String sortParameter) {
        if (sortParameter.equals("number")) {
            return sortByNumber(allPaymentDtos);
        } else if (sortParameter.equals("timeAsc")) {
            return sortByTimeAsc(allPaymentDtos);
        } else if (sortParameter.equals("timeDesc")){
            return sortByTimeDesc(allPaymentDtos);
        }
        return allPaymentDtos;
    }

    public List<PaymentDto> getAllSortedSentByAccountPaymentDtos(Integer moneyAccId, String sortParameter) {
        List<PaymentDto> allPaymentDtos = getAllSentByAccountPaymentDtos(moneyAccId);
        return getAllSortedPaymentDtos(allPaymentDtos, sortParameter);
    }

    public List<PaymentDto> getAllSortedReceivedByAccountPaymentDtos(Integer moneyAccId, String sortParameter) {
        List<PaymentDto> allPaymentDtos = getAllReceivedByAccountPaymentDtos(moneyAccId);
        return getAllSortedPaymentDtos(allPaymentDtos, sortParameter);
    }

    public List<PaymentDto> sortByNumber(List<PaymentDto> paymentDtos) {
        paymentDtos.sort(Comparator.comparing(PaymentDto::getNumber));
        return paymentDtos;
    }

    public List<PaymentDto> sortByTimeAsc(List<PaymentDto> paymentDtos) {
        paymentDtos.sort(Comparator.comparing(PaymentDto::getTimeString));
        return paymentDtos;
    }

    public List<PaymentDto> sortByTimeDesc(List<PaymentDto> paymentDtos) {
        paymentDtos.sort((paymentDtoOne, paymentDtoTwo) ->
                paymentDtoTwo.getTimeString().compareTo(paymentDtoOne.getTimeString()));
        return paymentDtos;
    }


    public PaymentDetailsDto getPaymentDetails(Long paymentNum, ResourceBundle rb) {
        Payment payment = paymentRepository.findByNumber(paymentNum);
        return convertPaymentToPaymentDetailsDto(payment, rb);
    }

    public PaymentDto convertPaymentToDto(Payment payment) {

        LocalDateTime paymentTime = payment.getTime();
        String timeString = paymentTime.toString().substring(0, 19);

        MoneyIntDecToStringAdapter paymentMoneyAdapter = new MoneyIntDecToStringAdapter(payment);

        PaymentDto paymentDto = new PaymentDto(payment.getPaymentId().getNumber(),
                payment.getStatus().equals(PaymentStatus.SENT), payment.getStatus().equals(PaymentStatus.PREPARED),
                paymentMoneyAdapter.getOperatedSumString(), paymentMoneyAdapter.getOperatedComissionString(),
                payment.getAssignment(), timeString);

        return paymentDto;
    }

    public List<PaymentDto> convertPaymentsToPaymentDtos(List<Payment> payments) {
        return payments.stream().map(payment -> convertPaymentToDto(payment)).collect(Collectors.toList());
    }

    public PaymentDetailsDto convertPaymentToPaymentDetailsDto(Payment payment, ResourceBundle rb) {
        LocalDateTime paymentTime = payment.getTime();
        String timeString = paymentTime.toString().substring(0, 19);
        String senderAccountName = payment.getMoneyAccount().getName();
        String receiverAccountName = "";
        String movedSumString = "";
        Transaction paymentTransaction = payment.getTransaction();
        if (paymentTransaction != null) {
            MoneyAccount receiverMoneyAccount = paymentTransaction.getReceiverMoneyAccount();
            if (receiverMoneyAccount != null) {
                receiverAccountName = receiverMoneyAccount.getName();
            } else {
                receiverAccountName = rb.getString("noRecMonAcc");
            }
            movedSumString = new MoneyIntDecToStringAdapter(paymentTransaction).getOperatedSumString();
        }

        MoneyIntDecToStringAdapter paymentMoneyAdapter = new MoneyIntDecToStringAdapter(payment);

        PaymentDetailsDto paymentDetailsDto = new PaymentDetailsDto(payment.getPaymentId().getNumber(),
                payment.getStatus().equals(PaymentStatus.SENT), payment.getStatus().equals(PaymentStatus.PREPARED),
                paymentMoneyAdapter.getOperatedSumString(), paymentMoneyAdapter.getOperatedComissionString(),
                movedSumString, payment.getAssignment(), timeString, senderAccountName, receiverAccountName);

        return paymentDetailsDto;
    }

    public CardPaymentConfirmationDto prepareToCardPayment(CardPaymentPreparationDto paymentPreparationDto, ResourceBundle rb)
            throws NoCreditCardByNumberException, BlockedAccountException, ToOwnRequisitePaymentException,
            NotEnoughMoneyOnAccountException {
        Long receiverCardNum = paymentPreparationDto.getReceiverCreditCardNumber();
        Integer senderMoneyAccountId = paymentPreparationDto.getSenderMoneyAccountId();
        CreditCard receiverCreditCard = creditCardService.getCreditCardByNumber(receiverCardNum);
        if (receiverCreditCard == null) {
            throw new NoCreditCardByNumberException();
        }
        if (receiverCreditCard.getMoneyAccount().getId() == senderMoneyAccountId) { // if payment on your own card
            throw new ToOwnRequisitePaymentException();
        }
        MoneyAccount senderMoneyAccount = moneyAccountService.getMoneyAccountById(senderMoneyAccountId);
        MoneyAccount receiverMoneyAccount = receiverCreditCard.getMoneyAccount();
        if (!senderMoneyAccount.getActive().equals(MoneyAccountActStatus.ACTIVE)) {
            throw new BlockedAccountException(rb.getString("verification.payment.sender.blocked"));
        }
        if (!receiverMoneyAccount.getActive().equals(MoneyAccountActStatus.ACTIVE)) {
            throw new BlockedAccountException(rb.getString("verification.payment.receiver.blocked"));
        }

        MoneyStringToIntDecAdapter paymentMoneyAdapter = new MoneyStringToIntDecAdapter(paymentPreparationDto);

        long payedSumInt = paymentMoneyAdapter.getOperatedSumInt();
        int payedSumDec = paymentMoneyAdapter.getOperatedSumDec();

        long payedSum = payedSumInt * 100 + payedSumDec;
        long paymentComission = round((payedSum) * comissionPercentage);
        int paymentComissionDec = (int)(paymentComission % 100);
        long paymentComissionInt = (paymentComission - paymentComissionDec) / 100;
        long totalInt = payedSum + paymentComission;
        int totalDec = (int)(totalInt % 100);
        totalInt = (totalInt - totalDec) / 100;

        String payedSumString = totalInt + "." + totalDec;
        String movedSumString = payedSumInt + "." + payedSumDec;
        String paymentComissionString = paymentComissionInt + "." + paymentComissionDec;
        if (senderMoneyAccount.getCurSumAvailableInt() < totalInt ||
                (senderMoneyAccount.getCurSumAvailableInt() == totalInt && senderMoneyAccount.getCurSumAvailableDec() < totalDec)) {
            NotEnoughMoneyOnAccountException exception = new NotEnoughMoneyOnAccountException();
            exception.setPayedSumString(payedSumString);
            exception.setPaymentComissionString(paymentComissionString);
            throw exception;
        }

        long curSumAvailableInt = senderMoneyAccount.getCurSumAvailableInt() * 100 - totalInt * 100 +
                senderMoneyAccount.getCurSumAvailableDec() - totalDec;
        int curSumAvailableDec = (int)(curSumAvailableInt % 100);
        curSumAvailableInt = (curSumAvailableInt - curSumAvailableDec) / 100;

        senderMoneyAccount.setCurSumAvailableInt(curSumAvailableInt);
        senderMoneyAccount.setCurSumAvailableDec(curSumAvailableDec);

        long paymentNum = addPropService.getNextPaymentNum();
        PaymentId paymentId = new PaymentId(senderMoneyAccountId, paymentNum);

        Payment payment = new Payment(paymentId, PaymentStatus.PREPARED, totalInt, totalDec, paymentComissionInt, paymentComissionDec,
                paymentPreparationDto.getAssignment(), LocalDateTime.now());

        payment.setMoneyAccount(senderMoneyAccount);

        paymentRepository.save(payment);

        return new CardPaymentConfirmationDto(senderMoneyAccountId, receiverMoneyAccount.getId(),
                receiverCardNum, paymentNum, receiverMoneyAccount.getName(), payedSumString, paymentPreparationDto.getAssignment(),
                movedSumString, paymentComissionString);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized MoneyAccPaymentConfirmationDto prepareToMoneyAccountPayment(MoneyAccPaymentPreparationDto paymentPreparationDto,
                                                                       ResourceBundle rb)
            throws NoMoneyAccountByNumberException, ToOwnRequisitePaymentException, BlockedAccountException,
            NotEnoughMoneyOnAccountException {
        Long receiverMoneyAccNum = paymentPreparationDto.getReceiverMoneyAccountNumber();
        Integer senderMoneyAccountId = paymentPreparationDto.getSenderMoneyAccountId();
        MoneyAccount receiverMoneyAccount = moneyAccountService.getMoneyAccountByNumber(receiverMoneyAccNum);
        if (receiverMoneyAccount == null) {
            throw new NoMoneyAccountByNumberException();
        }
        if (receiverMoneyAccount.getId() == senderMoneyAccountId) {
            throw new ToOwnRequisitePaymentException();
        }
        MoneyAccount senderMoneyAccount = moneyAccountService.getMoneyAccountById(senderMoneyAccountId);
        if (!senderMoneyAccount.getActive().equals(MoneyAccountActStatus.ACTIVE)) {
            throw new BlockedAccountException(rb.getString("verification.payment.sender.blocked"));
        }
        if (!receiverMoneyAccount.getActive().equals(MoneyAccountActStatus.ACTIVE)) {
            throw new BlockedAccountException(rb.getString("verification.payment.receiver.blocked"));
        }

        MoneyStringToIntDecAdapter paymentMoneyAdapter = new MoneyStringToIntDecAdapter(paymentPreparationDto);

        long payedSumInt = paymentMoneyAdapter.getOperatedSumInt();
        int payedSumDec = paymentMoneyAdapter.getOperatedSumDec();

        long payedSum = payedSumInt * 100 + payedSumDec;
        long paymentComission = round((payedSum) * comissionPercentage);
        int paymentComissionDec = (int)(paymentComission % 100);
        long paymentComissionInt = (paymentComission - paymentComissionDec) / 100;
        long totalInt = payedSum + paymentComission;
        int totalDec = (int)(totalInt % 100);
        totalInt = (totalInt - totalDec) / 100;

        String payedSumString = payedSumInt + "." + payedSumDec;
        String totalString = totalInt + "." + totalDec;
        String paymentComissionString = paymentComissionInt + "." + paymentComissionDec;
        if (senderMoneyAccount.getCurSumAvailableInt() < totalInt ||
                (senderMoneyAccount.getCurSumAvailableInt() == totalInt && senderMoneyAccount.getCurSumAvailableDec() < totalDec)) {
            NotEnoughMoneyOnAccountException exception = new NotEnoughMoneyOnAccountException();
            exception.setPayedSumString(totalString);
            exception.setPaymentComissionString(paymentComissionString);
            throw exception;
        }

        long curSumAvailableInt = senderMoneyAccount.getCurSumAvailableInt() * 100 - totalInt * 100 +
                senderMoneyAccount.getCurSumAvailableDec() - totalDec;
        int curSumAvailableDec = (int)(curSumAvailableInt % 100);
        curSumAvailableInt = (curSumAvailableInt - curSumAvailableDec) / 100;

        senderMoneyAccount.setCurSumAvailableInt(curSumAvailableInt);
        senderMoneyAccount.setCurSumAvailableDec(curSumAvailableDec);

        long paymentNum = addPropService.getNextPaymentNum();
        PaymentId paymentId = new PaymentId(senderMoneyAccountId, paymentNum);

        Payment payment = new Payment(paymentId, PaymentStatus.PREPARED, totalInt, totalDec, paymentComissionInt, paymentComissionDec,
                paymentPreparationDto.getAssignment(), LocalDateTime.now());

        payment.setMoneyAccount(senderMoneyAccount);

        paymentRepository.save(payment);

        return new MoneyAccPaymentConfirmationDto(senderMoneyAccountId, receiverMoneyAccount.getId(), receiverMoneyAccNum,
                paymentNum, receiverMoneyAccount.getName(), totalString,  paymentPreparationDto.getAssignment(),
                payedSumString, paymentComissionString);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void confirmToCardPayment(CardPaymentConfirmationDto cardPaymentConfirmationDto) {
        PaymentSendingDto paymentSendingDto =
                convertCardPaymConfDtoToPaymSendDto(cardPaymentConfirmationDto);

        savePayment(paymentSendingDto);
    }

    public void confirmToMoneyAccountPayment(MoneyAccPaymentConfirmationDto moneyAccPaymentConfirmationDto) {
        PaymentSendingDto paymentSendingDto =
                convertMoneyAccPaymConfDtoToPaymSendDto(moneyAccPaymentConfirmationDto);

        savePayment(paymentSendingDto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void savePayment(PaymentSendingDto paymentSendingDto) {

        Integer receiverMoneyAccId = paymentSendingDto.getReceiverAccountId();
        Integer senderMoneyAccId = paymentSendingDto.getSenderAccountId();

        Map<Integer, MoneyAccountMonitor> moneyAccountMonitorsMap = moneyAccountService.getMoneyAccountMonitorsMap();

        MoneyAccountMonitor monitor1 = receiverMoneyAccId < senderMoneyAccId ?
                moneyAccountMonitorsMap.get(senderMoneyAccId) : moneyAccountMonitorsMap.get(receiverMoneyAccId);

        MoneyAccountMonitor monitor2 = receiverMoneyAccId > senderMoneyAccId ?
                moneyAccountMonitorsMap.get(senderMoneyAccId) : moneyAccountMonitorsMap.get(receiverMoneyAccId);

        synchronized (monitor1) {
            synchronized (monitor2) {
                sendPayment(paymentSendingDto);
            }
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void sendPayment(PaymentSendingDto paymentSendingDto) {
        Long paymentNum = paymentSendingDto.getPaymentNumber();
        Integer senderMoneyAccountId = paymentSendingDto.getSenderAccountId();
        Integer receiverMoneyAccountId = paymentSendingDto.getReceiverAccountId();

        long totalInt = paymentSendingDto.getPayedSumInt();
        int totalDec = paymentSendingDto.getPayedSumDec();
        long comissionInt = paymentSendingDto.getComissionInt();
        int comissionDec = paymentSendingDto.getComissionDec();
        long movedSum = totalInt * 100 - comissionInt * 100 + totalDec - comissionDec;
        int movedSumDec = (int) (movedSum % 100);
        long movedSumInt = (movedSum - movedSumDec) / 100;

        MoneyAccount receiverMoneyAccount = moneyAccountService.getMoneyAccountById(receiverMoneyAccountId);
        MoneyAccount senderMoneyAccount = moneyAccountService.getMoneyAccountById(senderMoneyAccountId);

        long senderSumInt = senderMoneyAccount.getSumInt() * 100 - totalInt * 100 +
                senderMoneyAccount.getSumDec() - totalDec;
        int senderSumDec = (int)(senderSumInt % 100);
        senderSumInt = (senderSumInt - senderSumDec) / 100;

        senderMoneyAccount.setSumInt(senderSumInt);
        senderMoneyAccount.setSumDec(senderSumDec);
        senderMoneyAccount.setCurSumAvailableInt(senderSumInt);
        senderMoneyAccount.setCurSumAvailableDec(senderSumDec);

        long recSumInt = receiverMoneyAccount.getSumInt() * 100 + movedSumInt * 100 +
                receiverMoneyAccount.getSumDec() + movedSumDec;
        int recSumDec = (int)(recSumInt % 100);
        recSumInt = (recSumInt - recSumDec) / 100;

        receiverMoneyAccount.setSumInt(recSumInt);
        receiverMoneyAccount.setSumDec(recSumDec);
        receiverMoneyAccount.setCurSumAvailableInt(recSumInt);
        receiverMoneyAccount.setCurSumAvailableDec(recSumDec);

        CreditCard senderCreditCard = senderMoneyAccount.getCreditCard();

        if (senderCreditCard != null) {
            senderCreditCard.setSumAvailableInt(senderSumInt);
            senderCreditCard.setSumAvailableDec(senderSumDec);
        }

        senderMoneyAccount.setCreditCard(senderCreditCard);

        CreditCard receiverCreditCard = receiverMoneyAccount.getCreditCard();

        if (receiverCreditCard != null) {
            receiverCreditCard.setSumAvailableInt(recSumInt);
            receiverCreditCard.setSumAvailableDec(recSumDec);
        }

        receiverMoneyAccount.setCreditCard(receiverCreditCard);

        PaymentId paymentId = new PaymentId(paymentSendingDto.getSenderAccountId(), paymentSendingDto.getPaymentNumber());
        Payment paymentToSend = new Payment(paymentId, PaymentStatus.SENT, totalInt, totalDec, comissionInt,
                comissionDec, paymentSendingDto.getAssignment(), LocalDateTime.now());

        paymentToSend.setMoneyAccount(senderMoneyAccount);

        TransactionId transactionId = new TransactionId(paymentNum, senderMoneyAccountId, receiverMoneyAccountId);

        Transaction transaction = new Transaction(transactionId, movedSumInt, movedSumDec, paymentToSend, receiverMoneyAccount);

        transactionService.save(transaction);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void cancelPayment(PaymentCancellationDto paymentCancellationDto) {
        Integer senderMoneyAccountId = paymentCancellationDto.getSenderMoneyAccountId();
        Long paymentNumber = paymentCancellationDto.getPaymentNumber();

        MoneyStringToIntDecAdapter paymentMoneyAdapter = new MoneyStringToIntDecAdapter(paymentCancellationDto);

        long payedSumInt = paymentMoneyAdapter.getOperatedSumInt();
        int payedSumDec = paymentMoneyAdapter.getOperatedSumDec();
        MoneyAccount senderMoneyAccount = moneyAccountService.getMoneyAccountById(senderMoneyAccountId);

        long curSumAvailableInt = (senderMoneyAccount.getCurSumAvailableInt() + payedSumInt) * 100
                + senderMoneyAccount.getCurSumAvailableDec() + payedSumDec;
        int curSumAvailableDec = (int)(curSumAvailableInt % 100);
        curSumAvailableInt = (curSumAvailableInt - curSumAvailableDec) / 100;
        senderMoneyAccount.setCurSumAvailableInt(curSumAvailableInt);
        senderMoneyAccount.setCurSumAvailableDec(curSumAvailableDec);

        moneyAccountService.save(senderMoneyAccount);
        paymentRepository.deleteById(new PaymentId(senderMoneyAccountId, paymentNumber));

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentSendingDto convertCardPaymConfDtoToPaymSendDto(CardPaymentConfirmationDto
                                                                                 cardPaymentConfirmationDto) {

        PaymentSendingDto paymentSendingDto = new PaymentSendingDto();
        paymentSendingDto.setSenderAccountId(cardPaymentConfirmationDto.getSenderMoneyAccountId());
        paymentSendingDto.setReceiverAccountId(cardPaymentConfirmationDto.getReceiverMoneyAccountId());
        paymentSendingDto.setPaymentNumber(cardPaymentConfirmationDto.getPaymentNumber());

        MoneyStringToIntDecAdapter cardToPaymentTransMoneyAdapter = new MoneyStringToIntDecAdapter(cardPaymentConfirmationDto);

        paymentSendingDto.setPayedSumInt(cardToPaymentTransMoneyAdapter.getOperatedSumInt());
        paymentSendingDto.setPayedSumDec(cardToPaymentTransMoneyAdapter.getOperatedSumDec());
        paymentSendingDto.setComissionInt(cardToPaymentTransMoneyAdapter.getOperatedComissionInt());
        paymentSendingDto.setComissionDec(cardToPaymentTransMoneyAdapter.getOperatedComissionDec());

        paymentSendingDto.setAssignment(cardPaymentConfirmationDto.getAssignment());

        return paymentSendingDto;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentSendingDto convertMoneyAccPaymConfDtoToPaymSendDto(MoneyAccPaymentConfirmationDto
                                                                                     moneyAccPaymentConfirmationDto) {
        PaymentSendingDto paymentSendingDto = new PaymentSendingDto();
        paymentSendingDto.setSenderAccountId(moneyAccPaymentConfirmationDto.getSenderMoneyAccountId());
        paymentSendingDto.setReceiverAccountId(moneyAccPaymentConfirmationDto.getReceiverMoneyAccountId());
        paymentSendingDto.setPaymentNumber(moneyAccPaymentConfirmationDto.getPaymentNumber());

        MoneyStringToIntDecAdapter moneyAccToPaymentTransMoneyAdapter = new MoneyStringToIntDecAdapter(moneyAccPaymentConfirmationDto);

        paymentSendingDto.setPayedSumInt(moneyAccToPaymentTransMoneyAdapter.getOperatedSumInt());
        paymentSendingDto.setPayedSumDec(moneyAccToPaymentTransMoneyAdapter.getOperatedSumDec());
        paymentSendingDto.setComissionInt(moneyAccToPaymentTransMoneyAdapter.getOperatedComissionInt());
        paymentSendingDto.setComissionDec(moneyAccToPaymentTransMoneyAdapter.getOperatedComissionDec());

        paymentSendingDto.setAssignment(moneyAccPaymentConfirmationDto.getAssignment());

        return paymentSendingDto;
    }


}
