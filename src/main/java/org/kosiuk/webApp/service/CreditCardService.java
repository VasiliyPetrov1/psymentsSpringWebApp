package org.kosiuk.webApp.service;

import org.kosiuk.webApp.dto.CreditCardConfirmationDto;
import org.kosiuk.webApp.dto.CreditCardDto;
import org.kosiuk.webApp.entity.*;
import org.kosiuk.webApp.repository.CreditCardRepository;
import org.kosiuk.webApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        CreditCard creditCard = new CreditCard(creditCardId, number, 0.0, cvv, expireDate, paymentSystem);

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
        List<CreditCardDto> creditCardDtos = new ArrayList<>();
        for (CreditCard curCreditCard : creditCards) {
            creditCardDtos.add(convertCreditCardToDto(curCreditCard));
        }

        return creditCardDtos;
    }

    public List<CreditCardDto> convertCreditCardsToCreditCardDtos(List<CreditCard> creditCards) {
        List<CreditCardDto> creditCardDtos = new ArrayList<>();
        for (CreditCard curCreditCard : creditCards) {
            creditCardDtos.add(convertCreditCardToDto(curCreditCard));
        }
        return creditCardDtos;
    }

    public CreditCard getCreditCardByNumber(Long number) {
        return creditCardRepository.findByNumber(number);
    }

    public CreditCardDto convertCreditCardToDto(CreditCard creditCard) {

        CreditCardDto creditCardDto = new CreditCardDto(creditCard.getCreditCardId().getCreditCardId(),
                creditCard.getNumber(), creditCard.getSumAvailable(), creditCard.getCvv(),
                creditCard.getExpireDate(), creditCard.getPaymentSystem().equals(PaymentSystem.VISA),
                creditCard.getPaymentSystem().equals(PaymentSystem.MASTERCARD), creditCard.getMoneyAccount().getId());

        return creditCardDto;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CreditCard putMoney(Integer id, Double sum) {

        CreditCard creditCard = creditCardRepository.findByCardId(id);
        creditCard.setSumAvailable(creditCard.getSumAvailable() + sum);
        MoneyAccount moneyAccount = creditCard.getMoneyAccount();
        moneyAccount.setSum(moneyAccount.getSum() + sum);
        creditCardRepository.save(creditCard);
        return creditCardRepository.findByCardId(id);

    }

}
