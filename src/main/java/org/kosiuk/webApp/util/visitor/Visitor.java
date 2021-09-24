package org.kosiuk.webApp.util.visitor;

import org.kosiuk.webApp.dto.*;

/**
 * This interface is needed for declaring operations communicated with each other
 * on composite structures of objects and helps to avoid code copying and fitting
 * similar operations implementations in each method of one entity.
 */
public interface Visitor<T> {

    /**
     * Performs some composite operation on user registration dto.
     * @param userRegDto user registration dto which needs some composite operations on it to be declared
     * @return Result of some composite operation.
     */
    T visitUserRegDto(UserRegistrationDto userRegDto);

    T visitUserCreationDto(UserCreationDto userCreationDto);

    T visitUserEditionDto(UserEditionDto userEditionDto);

    T visitUserLimEditionDto(UserLimitedEditionDto userLimitedEditionDto);

    T visitCreditCardOrderDto(CreditCardOrderDto creditCardOrderDto);

    T visitMessage(String message);

    T visitMoneyAccountName(String monAccNamr);

    T visitCardPaymentPreparationDto(CardPaymentPreparationDto cardPaymentPreparationDto);

    T visitMoneyAccPaymentPreparationDto(MoneyAccPaymentPreparationDto moneyAccPaymentPreparationDto);

}
