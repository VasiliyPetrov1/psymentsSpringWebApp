package org.kosiuk.webApp.service;

import org.kosiuk.webApp.dto.CreditCardConfirmationDto;
import org.kosiuk.webApp.dto.CreditCardDto;
import org.kosiuk.webApp.entity.*;
import org.kosiuk.webApp.repository.CreditCardRepository;
import org.kosiuk.webApp.util.sumConversion.MoneyIntDecToStringAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CreditCardService {

    @Value("${application.oneTrillion}")
    public long ONE_TRILLION;
    @Value("${application.visaCode}")
    public int VISA_CODE;
    @Value("${application.masterCardCode}")
    public int MASTERCARD_CODE;
    private final AdditionalPropertiesService addPropService;
    private final CreditCardRepository creditCardRepository;
    private final UserService userService;
    private final Random random;
    @Value("${application.creditCardPageSize}")
    private int pageSize;

    public CreditCardService(AdditionalPropertiesService addPropService, CreditCardRepository creditCardRepository,
                             UserService userService) {
        this.addPropService = addPropService;
        this.creditCardRepository = creditCardRepository;
        this.userService = userService;
        this.random = new Random();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CreditCard createCreditCard (Integer ownerId, Integer moneyAccountId, Long number, Integer cvv,
                                        String expireDateString, boolean isVisa, boolean isMasterCard) {
        Integer id = addPropService.getNextCreditCardIdVal();
        CreditCardId creditCardId = new CreditCardId(ownerId, moneyAccountId, id);
        PaymentSystem paymentSystem = null;
        if (isVisa) {
            paymentSystem = PaymentSystem.VISA;
            addPropService.incCurVisaCardNum();
        } else if (isMasterCard) {
            paymentSystem = PaymentSystem.MASTERCARD;
            addPropService.incCurMasterCardNum();
        }
        String[] yearMonthDay = expireDateString.split("-");
        LocalDate expireDate = LocalDate.of(Integer.parseInt(yearMonthDay[0]), Integer.parseInt(yearMonthDay[1]),
                Integer.parseInt(yearMonthDay[2]));
        CreditCard creditCard = new CreditCard(creditCardId, number, 0L, 0, cvv, expireDate, paymentSystem);

        User user = userService.getUserById(ownerId);
        creditCard.setUser(user);

        creditCardRepository.save(creditCard);
        return creditCard;
    }

    public CreditCardConfirmationDto getNewCreditCardConfDto (boolean isVisa, boolean isMasterCard) {
        CreditCardConfirmationDto creditCardConfirmationDto = new CreditCardConfirmationDto();
        long number = 0;
        if (isVisa) {
            number = VISA_CODE * ONE_TRILLION + addPropService.getNextCurVisaCardNum();
        } else if (isMasterCard) {
            number = MASTERCARD_CODE * ONE_TRILLION + addPropService.getNextCurMasterCardNum();
        }
        creditCardConfirmationDto.setNumber(number);
        creditCardConfirmationDto.setVisa(isVisa);
        creditCardConfirmationDto.setMasterCard(isMasterCard);
        creditCardConfirmationDto.setCvv(random.nextInt(899) + 100);
        creditCardConfirmationDto.setExpireDateString(LocalDate.now().plusYears(3).toString());
        return creditCardConfirmationDto;
    }

    public Page<CreditCard> getAllUsersCreditCardsPage(int pageNumber, User user) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return creditCardRepository.findAllByUser(user, pageable);
    }

    public List<CreditCardDto> getAllUsersCreditCardDtos (User user) {
        List<CreditCard> creditCards = user.getCreditCards();
        return creditCards.stream().map(creditCard -> convertCreditCardToDto(creditCard)).collect(Collectors.toList());
    }

    public List<CreditCardDto> convertCreditCardsToCreditCardDtos(List<CreditCard> creditCards) {
        return creditCards.stream().map(creditCard -> convertCreditCardToDto(creditCard)).collect(Collectors.toList());
    }

    public CreditCard getCreditCardByNumber(Long number) {
        return creditCardRepository.findByNumber(number);
    }

    public CreditCardDto convertCreditCardToDto(CreditCard creditCard) {

        CreditCardDto creditCardDto = new CreditCardDto(creditCard.getCreditCardId().getCreditCardId(),
                creditCard.getNumber(), new MoneyIntDecToStringAdapter(creditCard).getOperatedSumString(), creditCard.getCvv(),
                creditCard.getExpireDate(), creditCard.getPaymentSystem().equals(PaymentSystem.VISA),
                creditCard.getPaymentSystem().equals(PaymentSystem.MASTERCARD), creditCard.getMoneyAccount().getId());

        return creditCardDto;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CreditCard putMoney(Integer id, String sumString) {

        String[] sumIntDec = sumString.split("\\.");

        long sumInt = Long.parseLong(sumIntDec[0]);
        int sumDec = Integer.parseInt(sumIntDec[1]);

        CreditCard creditCard = creditCardRepository.findByCardId(id);
        long sumPrevInt = creditCard.getSumAvailableInt();
        int sumPrevDec = creditCard.getSumAvailableDec();
        long sum = (sumInt + sumPrevInt) * 100 + sumDec + sumPrevDec;
        System.err.println(sum);
        sumPrevDec = (int)(sum % 100);
        sumPrevInt = (sum - sumPrevDec) / 100;
        System.err.println(sumPrevDec);
        creditCard.setSumAvailableInt(sumPrevInt);
        creditCard.setSumAvailableDec(sumPrevDec);

        MoneyAccount moneyAccount = creditCard.getMoneyAccount();
        long curSumAvPrevInt = moneyAccount.getCurSumAvailableInt();
        int curSumAvPrevDec = moneyAccount.getCurSumAvailableDec();
        sum = (sumInt + curSumAvPrevInt) * 100 + sumDec + curSumAvPrevDec;
        int curSumAvailableDec = (int)(sum % 100);
        long curSumAvailableInt = (sum - sumDec) / 100;

        moneyAccount.setCurSumAvailableInt(curSumAvailableInt);
        moneyAccount.setCurSumAvailableDec(curSumAvailableDec);
        moneyAccount.setSumInt(sumPrevInt);
        moneyAccount.setSumDec(sumPrevDec);
        creditCardRepository.save(creditCard);
        return creditCardRepository.findByCardId(id);

    }

}
