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
    private final CompositeValidator<String> monAccNameValidator;
    private final CompositeValidator<String> cardNumStringValidator;
    private final CompositeValidator<String> sumStringValidator;
    private final CompositeValidator<String> assignmentValidator;
    private final CompositeValidator<String> moneyAccNumStringValidator;
    private final CompositeValidator<List<Boolean>> userRolesValidator;
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
        userRolesValidator = new CompositeValidator<>(
                new NotAllFlagsFalseValidator(rb.getString("validation.user.roles.notBlank"))
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
        cardNumStringValidator = new CompositeValidator<>(
                new SizeValidator(16, 16, rb.getString("validation.payment.cardNum.size")),
                new IsNumberValidator(rb.getString("validation.payment.cardNum.isNumber")),
                new NotBlankValidator(rb.getString("validation.payment.cardNum.notBlank"))
        );
        sumStringValidator = new CompositeValidator<>(
                new IsMoneySumValidator(rb.getString("validation.payment.payedSum.isMoneySum")),
                new NotBlankValidator(rb.getString("validation.payment.payedSum.notBlank"))
        );
        assignmentValidator = new CompositeValidator<>(
                new SizeValidator(1, 45, rb.getString("validation.payment.assignment.size")),
                new NotBlankValidator(rb.getString("validation.payment.assignment.notBlank"))
        );
        moneyAccNumStringValidator = new CompositeValidator<>(
                new SizeValidator(12, 12, rb.getString("validation.payment.moneyAccNum.size")),
                new IsNumberValidator(rb.getString("validation.payment.cardNum.isNumber")),
                new NotBlankValidator(rb.getString("validation.payment.moneyAccNum.notBlank"))
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
        result = userRolesValidator.validate(List.of(userCreationDto.isUser(), userCreationDto.isAdmin()));
        if (!result.isValid()) {
            validationErrorsMap.put("rolesErrors", result.getMessage().split("\n"));
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
        result = userRolesValidator.validate(List.of(userEditionDto.isUser(), userEditionDto.isAdmin()));
        if (!result.isValid()) {
            validationErrorsMap.put("rolesErrors", result.getMessage().split("\n"));
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
        CompositeValidator<String> messageValidator = new CompositeValidator<>(
                new SizeValidator(0, 100, rb.getString("validation.order.rejectionMessage.size"))
        );
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

    @Override
    public Map<String, String[]> visitSumString(String moneySumString) {
        Map<String, String[]> validationErrorsMap = new HashMap<>();

        Result result = sumStringValidator.validate(moneySumString);

        if (!result.isValid()) {
            validationErrorsMap.put("sumErrors", result.getMessage().split("\n"));
        }

        return validationErrorsMap;
    }

    @Override
    public Map<String, String[]> visitCardPaymentPreparationDto(CardPaymentPreparationDto cardPaymentPreparationDto) {
        Map<String, String[]> validationErrorsMap = new HashMap<>();
        Result result = cardNumStringValidator.validate(String.valueOf(cardPaymentPreparationDto.getReceiverCreditCardNumber()));
        if (!result.isValid()) {
            validationErrorsMap.put("cardNumErrors", result.getMessage().split("\n"));
        }
        result = sumStringValidator.validate(cardPaymentPreparationDto.getPayedSumString());
        if (!result.isValid()) {
            validationErrorsMap.put("sumErrors", result.getMessage().split("\n"));
        }
        result = assignmentValidator.validate(cardPaymentPreparationDto.getAssignment());
        if (!result.isValid()) {
            validationErrorsMap.put("assignmentErrors", result.getMessage().split("\n"));
        }
        return validationErrorsMap;
    }

    @Override
    public Map<String, String[]> visitMoneyAccPaymentPreparationDto(MoneyAccPaymentPreparationDto moneyAccPaymentPreparationDto) {
        Map<String, String[]> validationErrorsMap = new HashMap<>();

        Result result = moneyAccNumStringValidator.validate(String.valueOf(moneyAccPaymentPreparationDto.getReceiverMoneyAccountNumber()));
        if (!result.isValid()) {
            validationErrorsMap.put("monAccNumErrors", result.getMessage().split("\n"));
        }
        result = sumStringValidator.validate(moneyAccPaymentPreparationDto.getPayedSumString());
        if (!result.isValid()) {
            validationErrorsMap.put("sumErrors", result.getMessage().split("\n"));
        }
        result = assignmentValidator.validate(moneyAccPaymentPreparationDto.getAssignment());
        if (!result.isValid()) {
            validationErrorsMap.put("assignmentErrors", result.getMessage().split("\n"));
        }

        return validationErrorsMap;
    }
}
