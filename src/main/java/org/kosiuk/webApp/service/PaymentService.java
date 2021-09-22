package org.kosiuk.webApp.service;

import org.kosiuk.webApp.dto.*;
import org.kosiuk.webApp.entity.*;
import org.kosiuk.webApp.exceptions.*;
import org.kosiuk.webApp.paymentSendingObserver.PaymentSendingDto;
import org.kosiuk.webApp.paymentSendingObserver.PaymentSendingManager;
import org.kosiuk.webApp.repository.PaymentRepository;
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
    private final Queue<PaymentAndTransactionDto> paymentsOnNumberSetUpQueue;
    private final PaymentSendingManager paymentSendingManager;
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
        this.paymentSendingManager = PaymentSendingManager.getInstance();
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


    public PaymentDetailsDto getPaymentDetails(Long paymentNum) {
        Payment payment = paymentRepository.findByNumber(paymentNum);
        return convertPaymentToPaymentDetailsDto(payment);
    }

    public PaymentDto convertPaymentToDto(Payment payment) {

        LocalDateTime paymentTime = payment.getTime();
        String timeString = paymentTime.toString().substring(0, 19);

        PaymentDto paymentDto = new PaymentDto(payment.getPaymentId().getNumber(),
                payment.getStatus().equals(PaymentStatus.SENT), payment.getStatus().equals(PaymentStatus.PREPARED),
                payment.getPayedSum(), payment.getComission(), payment.getAssignment(), timeString);

        return paymentDto;
    }

    public List<PaymentDto> convertPaymentsToPaymentDtos(List<Payment> payments) {
        return payments.stream().map(payment -> convertPaymentToDto(payment)).collect(Collectors.toList());
    }

    public PaymentDetailsDto convertPaymentToPaymentDetailsDto(Payment payment) {
        LocalDateTime paymentTime = payment.getTime();
        String timeString = paymentTime.toString().substring(0, 19);
        String senderAccountName = payment.getMoneyAccount().getName();
        String receiverAccountName = null;
        Double movedSum = null;
        Transaction paymentTransaction = payment.getTransaction();
        if (paymentTransaction != null) {
            receiverAccountName = paymentTransaction.getReceiverMoneyAccount().getName();
            movedSum = paymentTransaction.getMovedSum();
        }

        PaymentDetailsDto paymentDetailsDto = new PaymentDetailsDto(payment.getPaymentId().getNumber(),
                payment.getStatus().equals(PaymentStatus.SENT), payment.getStatus().equals(PaymentStatus.PREPARED),
                payment.getPayedSum(), payment.getComission(), movedSum, payment.getAssignment(), timeString,
                senderAccountName, receiverAccountName);

        return paymentDetailsDto;
    }

    public CardPaymentConfirmationDto prepareToCardPayment(CardPaymentPreparationDto paymentPreparationDto)
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
            throw new BlockedAccountException("Your money account is blocked");
        }
        if (!receiverMoneyAccount.getActive().equals(MoneyAccountActStatus.ACTIVE)) {
            throw new BlockedAccountException("Receiver money account is blocked");
        }

        double payedSum = paymentPreparationDto.getPayedSum();
        double paymentComission = (round(payedSum * comissionPercentage * 100)) / 100.0;
        payedSum = payedSum + paymentComission;

        if (senderMoneyAccount.getSum() < payedSum) {
            NotEnoughMoneyOnAccountException exception = new NotEnoughMoneyOnAccountException();
            exception.setPayedSum(payedSum);
            exception.setPaymentComission(paymentComission);
            throw exception;
        }

        return new CardPaymentConfirmationDto(senderMoneyAccountId,
                receiverCardNum, receiverMoneyAccount.getName(), payedSum, paymentPreparationDto.getAssignment(),
                paymentComission);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public MoneyAccPaymentConfirmationDto prepareToMoneyAccountPayment(MoneyAccPaymentPreparationDto paymentPreparationDto)
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
            throw new BlockedAccountException("Your money account is blocked");
        }
        if (!receiverMoneyAccount.getActive().equals(MoneyAccountActStatus.ACTIVE)) {
            throw new BlockedAccountException("Receiver money account is blocked");
        }

        double payedSum = paymentPreparationDto.getPayedSum();
        double paymentComission = (round(payedSum * comissionPercentage * 100)) / 100.0;
        payedSum = payedSum + paymentComission;

        if (senderMoneyAccount.getSum() < payedSum) {
            NotEnoughMoneyOnAccountException exception = new NotEnoughMoneyOnAccountException();
            exception.setPayedSum(payedSum);
            exception.setPaymentComission(paymentComission);
            throw exception;
        }

        MoneyAccPaymentConfirmationDto moneyAccPaymentConfirmationDto =
                new MoneyAccPaymentConfirmationDto(senderMoneyAccountId, receiverMoneyAccNum,
                        receiverMoneyAccount.getName(), payedSum, paymentPreparationDto.getAssignment(),
                        paymentComission);

        return moneyAccPaymentConfirmationDto;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void confirmToCardPayment(CardPaymentConfirmationDto cardPaymentConfirmationDto) {
        PaymentAndTransactionDto paymentAndTransactionDto =
                convertCardPaymConfDtoToPaymTransDto(cardPaymentConfirmationDto);

        savePayment(paymentAndTransactionDto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void confirmToMoneyAccountPayment(MoneyAccPaymentConfirmationDto moneyAccPaymentConfirmationDto) {
        PaymentAndTransactionDto paymentAndTransactionDto =
                convertMoneyAccPaymConfDtoToPaymTransDto(moneyAccPaymentConfirmationDto);

        savePayment(paymentAndTransactionDto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void savePayment(PaymentAndTransactionDto paymentAndTransactionDto) {
        if (paymentNumSetupPermission) {
            paymentNumSetupPermission = false;
            long paymentNum = addPropService.getNextPaymentNum();
            Payment preparedPayment = new Payment();

            PaymentId confPaymentId = new PaymentId();
            confPaymentId.setNumber(paymentNum);
            Integer senderMoneyAccId = paymentAndTransactionDto.getSenderAccountId();
            confPaymentId.setSenderMoneyAccountId(senderMoneyAccId);
            MoneyAccount senderMoneyAccount = moneyAccountService.getMoneyAccountById(senderMoneyAccId);
            preparedPayment.setMoneyAccount(senderMoneyAccount);
            preparedPayment.setPaymentId(confPaymentId);

            preparedPayment.setPayedSum(paymentAndTransactionDto.getPayedSum());
            preparedPayment.setComission(paymentAndTransactionDto.getComission());
            preparedPayment.setStatus(PaymentStatus.PREPARED);
            preparedPayment.setAssignment(paymentAndTransactionDto.getAssignment());
            preparedPayment.setTime(LocalDateTime.now());
            paymentRepository.save(preparedPayment); // payment was saved as prepared and will be used later
            // for creating transaction and altering moneySum on accounts

            paymentNumSetupPermission = true;

            PaymentSendingDto paymentSendingDto = new PaymentSendingDto();
            paymentSendingDto.setPaymentNumber(paymentNum);
            paymentSendingDto.setSenderAccountId(senderMoneyAccId);
            Integer receiverMoneyAccId = paymentAndTransactionDto.getReceiverAccountId();
            paymentSendingDto.setReceiverAccountId(receiverMoneyAccId);
            paymentSendingDto.setPaymentSendingManager(paymentSendingManager);
            paymentSendingDto.setPaymentService(this);

            //sendPayment(paymentSendingDto);

            paymentSendingManager.subscribe(paymentSendingDto, senderMoneyAccId);
            paymentSendingManager.subscribe(paymentSendingDto, receiverMoneyAccId);

            paymentSendingDto.send();

            PaymentAndTransactionDto curDtoToSave = paymentsOnNumberSetUpQueue.poll();
            if (curDtoToSave != null) {
                savePayment(curDtoToSave);
            } else if (isPaymentDtoOnAdding) {
                while (isPaymentDtoOnAdding) {
                }
                curDtoToSave = paymentsOnNumberSetUpQueue.poll();
                if (curDtoToSave != null) {
                    savePayment(curDtoToSave);
                }
            }
        } else {
            synchronized (paymentsOnNumberSetUpQueue) {
                isPaymentDtoOnAdding = true;
                paymentsOnNumberSetUpQueue.add(paymentAndTransactionDto);
                isPaymentDtoOnAdding = false;
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void sendPayment(PaymentSendingDto paymentSendingDto) {
        Long paymentNum = paymentSendingDto.getPaymentNumber();
        Integer senderMoneyAccountId = paymentSendingDto.getSenderAccountId();
        Integer receiverMoneyAccountId = paymentSendingDto.getReceiverAccountId();
        Payment paymentToSend = paymentRepository.findByNumber(paymentNum);

        double payedSum = paymentToSend.getPayedSum();
        double comission = paymentToSend.getComission();
        double movedSum = payedSum - comission;
        MoneyAccount receiverMoneyAccount = moneyAccountService.getMoneyAccountById(receiverMoneyAccountId);
        MoneyAccount senderMoneyAccount = moneyAccountService.getMoneyAccountById(senderMoneyAccountId);

        senderMoneyAccount.setSum(senderMoneyAccount.getSum() - payedSum);
        receiverMoneyAccount.setSum(receiverMoneyAccount.getSum() + movedSum);

        CreditCard senderCreditCard = senderMoneyAccount.getCreditCard();
        if (senderCreditCard != null) {
            senderCreditCard.setSumAvailable(senderCreditCard.getSumAvailable() - payedSum);
        }
        senderMoneyAccount.setCreditCard(senderCreditCard);
        CreditCard receiverCreditCard = receiverMoneyAccount.getCreditCard();
        if (receiverCreditCard != null) {
            receiverCreditCard.setSumAvailable(receiverCreditCard.getSumAvailable() + movedSum);
        }
        receiverMoneyAccount.setCreditCard(receiverCreditCard);

        paymentToSend.setMoneyAccount(senderMoneyAccount);
        paymentToSend.setStatus(PaymentStatus.SENT);
        paymentToSend.setTime(LocalDateTime.now());

        Transaction transaction = new Transaction();
        TransactionId transactionId = new TransactionId();
        transactionId.setPaymentNumber(paymentNum);
        transactionId.setSenderMoneyAccountId(senderMoneyAccountId);
        transactionId.setReceiverMoneyAccountId(receiverMoneyAccountId);
        transaction.setTransactionId(transactionId);
        transaction.setMovedSum(movedSum);
        transaction.setPayment(paymentToSend);
        transaction.setReceiverMoneyAccount(receiverMoneyAccount);
        transactionService.save(transaction);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentAndTransactionDto convertCardPaymConfDtoToPaymTransDto(CardPaymentConfirmationDto
                                                                                 cardPaymentConfirmationDto) {

        PaymentAndTransactionDto paymentAndTransactionDto = new PaymentAndTransactionDto();
        Long receiverCreditCardNum = cardPaymentConfirmationDto.getReceiverCreditCardNumber();
        Integer receiverMoneyAccId = creditCardService.getCreditCardByNumber(receiverCreditCardNum).
                getMoneyAccount().getId();
        paymentAndTransactionDto.setSenderAccountId(cardPaymentConfirmationDto.getSenderMoneyAccountId());
        paymentAndTransactionDto.setReceiverAccountId(receiverMoneyAccId);
        paymentAndTransactionDto.setPayedSum(cardPaymentConfirmationDto.getPayedSum());
        paymentAndTransactionDto.setComission(cardPaymentConfirmationDto.getPaymentComission());
        paymentAndTransactionDto.setAssignment(cardPaymentConfirmationDto.getAssignment());

        return paymentAndTransactionDto;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentAndTransactionDto convertMoneyAccPaymConfDtoToPaymTransDto(MoneyAccPaymentConfirmationDto
                                                                                     moneyAccPaymentConfirmationDto) {
        PaymentAndTransactionDto paymentAndTransactionDto = new PaymentAndTransactionDto();
        Long receiverMoneyAccNum = moneyAccPaymentConfirmationDto.getReceiverMoneyAccountNumber();
        Integer receiverMoneyAccId = moneyAccountService.getMoneyAccountByNumber(receiverMoneyAccNum).getId();
        paymentAndTransactionDto.setSenderAccountId(moneyAccPaymentConfirmationDto.getSenderMoneyAccountId());
        paymentAndTransactionDto.setReceiverAccountId(receiverMoneyAccId);
        paymentAndTransactionDto.setPayedSum(moneyAccPaymentConfirmationDto.getPayedSum());
        paymentAndTransactionDto.setComission(moneyAccPaymentConfirmationDto.getPaymentComission());
        paymentAndTransactionDto.setAssignment(moneyAccPaymentConfirmationDto.getAssignment());

        return paymentAndTransactionDto;
    }


}
