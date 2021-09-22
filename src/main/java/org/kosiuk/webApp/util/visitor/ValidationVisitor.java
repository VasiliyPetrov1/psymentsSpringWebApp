package org.kosiuk.webApp.util.visitor;

import org.kosiuk.webApp.dto.*;
import org.kosiuk.webApp.util.validator.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This class is needed for fitting different validation behaviours of different objects in it.
 */

public class ValidationVisitor implements Visitor<Map<String, String[]>>{

    private final ResourceBundle rb;
    private final CompositeValidator<String> usernameValidator;
    private final CompositeValidator<String> emailValidator;
    private final CompositeValidator<String> passwordValidator;
    private final CompositeValidator<String> messageValidator;
    private final CompositeValidator<List<Boolean>> paymentSystemValidator;
    CompositeValidator<String> monAccNameValidator;

    public ValidationVisitor(ResourceBundle rb) {
        this.rb = rb;
        usernameValidator = new CompositeValidator<> (
                new NotBlankValidator(rb.getString("validation.user.username.notBlank")),
                new SizeValidator(2, 30, rb.getString("validation.user.username.size"))
        );
        emailValidator = new CompositeValidator<> (
                new NotBlankValidator(rb.getString("validation.user.email.notBlank")),
                new EmailAddressValidator(rb.getString("validation.user.email.invalid"))
        );
        passwordValidator = new CompositeValidator<> (
                new NotBlankValidator(rb.getString("validation.user.password.notBlank")),
                new SizeValidator(4, 16, rb.getString("validation.user.password.size"))
        );
        messageValidator = new CompositeValidator<>(
                new SizeValidator(0, 100, rb.getString("validation.order.message.size"))
        );
        paymentSystemValidator = new CompositeValidator<>(
                new NotAllFlagsFalseValidator(rb.getString("validation.order.noPaymentSystem")),
                new NotMoreThanOneFlagTrueValidator(rb.getString("validation.order.notDistinctPaymentSystem"))
        );
        monAccNameValidator = new CompositeValidator<>(
                new NotBlankValidator(rb.getString("validation.moneyAccount.name.notBlank")),
                new SizeValidator(0, 45, rb.getString("validation.moneyAccount.name.size"))
        );
    }

    /**
     * Performs validation of user registration dto.
     * @param userRegDto object to be validated
     * @return validationErrorsMap
     */
    @Override
    public Map<String, String[]> visitUserRegDto(UserRegistrationDto userRegDto) {
        Map<String, String[]> validationErrorsMap = new HashMap<>();

        Result result = usernameValidator.validate(userRegDto.getUsername());
        if (!result.isValid()) {
            validationErrorsMap.put("usernameErrors", result.getMessage().split("\n"));
        }
        result = emailValidator.validate(userRegDto.getEmail());
        if (!result.isValid()) {
            validationErrorsMap.put("emailErrors", result.getMessage().split("\n"));
        }
        result = passwordValidator.validate(userRegDto.getPassword());
        if (!result.isValid()) {
            validationErrorsMap.put("passwordErrors", result.getMessage().split("\n"));
        }

        return validationErrorsMap;
    }

    @Override
    public Map<String, String[]> visitUserCreationDto(UserCreationDto userCreationDto) {
        Map<String, String[]> validationErrorsMap = new HashMap<>();

        Result result = usernameValidator.validate(userCreationDto.getUsername());
        if (!result.isValid()) {
            validationErrorsMap.put("usernameErrors", result.getMessage().split("\n"));
        }
        result = emailValidator.validate(userCreationDto.getEmail());
        if (!result.isValid()) {
            validationErrorsMap.put("emailErrors", result.getMessage().split("\n"));
        }
        result = passwordValidator.validate(userCreationDto.getPassword());
        if (!result.isValid()) {
            validationErrorsMap.put("passwordErrors", result.getMessage().split("\n"));
        }

        return validationErrorsMap;
    }

    @Override
    public Map<String, String[]> visitUserEditionDto(UserEditionDto userEditionDto) {
        Map<String, String[]> validationErrorsMap = new HashMap<>();

        Result result = usernameValidator.validate(userEditionDto.getUsername());
        if (!result.isValid()) {
            validationErrorsMap.put("usernameErrors", result.getMessage().split("\n"));
        }
        result = emailValidator.validate(userEditionDto.getEmail());
        if (!result.isValid()) {
            validationErrorsMap.put("emailErrors", result.getMessage().split("\n"));
        }

        return validationErrorsMap;
    }

    @Override
    public Map<String, String[]> visitUserLimEditionDto(UserLimitedEditionDto userLimEditionDto) {
        Map<String, String[]> validationErrorsMap = new HashMap<>();

        Result result = usernameValidator.validate(userLimEditionDto.getUsername());
        if (!result.isValid()) {
            validationErrorsMap.put("usernameErrors", result.getMessage().split("\n"));
        }
        result = emailValidator.validate(userLimEditionDto.getEmail());
        if (!result.isValid()) {
            validationErrorsMap.put("emailErrors", result.getMessage().split("\n"));
        }
        result = passwordValidator.validate(userLimEditionDto.getPassword());
        if (!result.isValid()) {
            validationErrorsMap.put("passwordErrors", result.getMessage().split("\n"));
        }

        return validationErrorsMap;
    }

    @Override
    public Map<String, String[]> visitCreditCardOrderDto(CreditCardOrderDto creditCardOrderDto) {
        Map<String, String[]> validationErrorsMap = new HashMap<>();
        Result result = messageValidator.validate(creditCardOrderDto.getMessage());
        if (!result.isValid()) {
            validationErrorsMap.put("messageErrors", result.getMessage().split("\n"));
        }
        result = paymentSystemValidator.validate(List.of(creditCardOrderDto.isVisa(), creditCardOrderDto.isMasterCard()));
        if (!result.isValid()) {
            validationErrorsMap.put("paymentSystemErrors", result.getMessage().split("\n"));
        }
        return validationErrorsMap;
    }

    @Override
    public Map<String, String[]> visitMessage(String message) {
        Map<String, String[]> validationErrorsMap = new HashMap<>();
        Result result = messageValidator.validate(message);
        if (!result.isValid()) {
            validationErrorsMap.put("messageErrors", result.getMessage().split("\n"));
        }
        return validationErrorsMap;
    }

    @Override
    public Map<String, String[]> visitMoneyAccountName(String moneyAccountName) {
        Map<String, String[]> validationErrorsMap = new HashMap<>();
        Result result = monAccNameValidator.validate(moneyAccountName);
        if (!result.isValid()) {
            validationErrorsMap.put("nameErrors", result.getMessage().split("\n"));
        }
        return validationErrorsMap;
    }


}
