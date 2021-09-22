package org.kosiuk.webApp.controller;

import org.hibernate.PropertyValueException;
import org.kosiuk.webApp.dto.CreditCardConfirmationDto;
import org.kosiuk.webApp.dto.CreditCardOrderDto;
import org.kosiuk.webApp.dto.CreditCardOrderWithUserDto;
import org.kosiuk.webApp.dto.MoneyAccountConfirmationDto;
import org.kosiuk.webApp.entity.CreditCardOrder;
import org.kosiuk.webApp.entity.OrderStatus;
import org.kosiuk.webApp.entity.PaymentSystem;
import org.kosiuk.webApp.entity.User;
import org.kosiuk.webApp.exceptions.UnsafeMoneyAccCreationException;
import org.kosiuk.webApp.exceptions.UnsafeOrderConfirmationException;
import org.kosiuk.webApp.service.CreditCardOrderService;
import org.kosiuk.webApp.service.CreditCardService;
import org.kosiuk.webApp.service.MoneyAccountService;
import org.kosiuk.webApp.service.UserService;
import org.kosiuk.webApp.util.AuthUtil;
import org.kosiuk.webApp.util.visitor.ValidationVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
@RequestMapping("/app/order")
public class CreditCardOrderController {

    private final CreditCardOrderService creditCardOrderService;
    private final UserService userService;
    private final CreditCardService creditCardService;
    private final MoneyAccountService moneyAccountService;

    @Autowired
    public CreditCardOrderController(CreditCardOrderService creditCardOrderService, CreditCardService creditCardService,
                                     MoneyAccountService moneyAccountService, UserService userService) {
        this.userService = userService;
        this.creditCardOrderService = creditCardOrderService;
        this.creditCardService = creditCardService;
        this.moneyAccountService = moneyAccountService;
    }

    @PostMapping("{userId}")
    public String createNewOrderForUser(@PathVariable Integer userId,
                                        @ModelAttribute("newOrderCreationDto")CreditCardOrderDto creditCardOrderDto,
                                        Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        ResourceBundle rb = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        ValidationVisitor validationVisitor = new ValidationVisitor(rb);
        Map<String, String[]> validationErrorsMap = (Map<String, String[]>) creditCardOrderDto.accept(validationVisitor);

        User user = userService.getUserById(userId);

        if(!validationErrorsMap.isEmpty()) {
            model.addAttribute("userLimEditionDto", userService.convertUserToLimDto(user));
            model.addAttribute("creditCardOrderDtos", creditCardOrderService.getAllUsersOrderDtos(user));
            model.addAttribute("newOrderCreationDto", creditCardOrderDto);
            model.addAttribute("errors", validationErrorsMap);
            return "personalRoom";
        }

        creditCardOrderService.createCreditCardOrder(creditCardOrderDto, userId);

        return "redirect:/app/personalRoom/" + userId;
    }

    @GetMapping()
    public String showAllOrders (Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        return showAllOrdersPage(1, model);
    }

    @GetMapping("/page/{pageNumber}")
    public String showAllOrdersPage (@PathVariable("pageNumber") Integer pageNumber,
                                     Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);

        Page<CreditCardOrder> page = creditCardOrderService.getAllOrdersPage(pageNumber);
        List<CreditCardOrder> orders = page.getContent();

        model.addAttribute("curPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        List<CreditCardOrderWithUserDto> creditCardOrderWithUserDtos =
                creditCardOrderService.convertCreditCardOrdersToWithUserDtos(orders);

        model.addAttribute("creditCardOrderWithUserDtos", creditCardOrderWithUserDtos);

        return "showAllOrders";

    }

    @GetMapping("{orderId}/reject")
    public String getRejectionForm(@PathVariable("orderId") Integer orderId,
                                   @RequestParam("ownerId") Integer ownerId,
                                   @RequestParam("message") String message,
                                   Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);

        model.addAttribute("orderId", orderId);
        model.addAttribute("ownerId", ownerId);
        model.addAttribute("message", message);
        model.addAttribute("errors", new HashMap<String, String[]>());

        return "orderRejectionForm";

    }

    @PatchMapping("{orderId}/reject")
    public String rejectOrder (@PathVariable("orderId") Integer orderId,
                               @RequestParam("ownerId") Integer ownerId,
                               @RequestParam("rejectMessage") String rejectMessage,
                               Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        ResourceBundle rb = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        ValidationVisitor validationVisitor = new ValidationVisitor(rb);
        Map<String, String[]> validationErrorsMap = validationVisitor.visitMessage(rejectMessage);

        if(!validationErrorsMap.isEmpty()) {
            model.addAttribute("ownerId", ownerId);
            model.addAttribute("orderId", orderId);
            model.addAttribute("rejectMessage", rejectMessage);
            model.addAttribute("errors", validationErrorsMap);
            return "orderRejectionForm";
        }

        creditCardOrderService.rejectCreditCardOrder(orderId, ownerId, rejectMessage);
        return "redirect:/app/order";
    }

    @GetMapping("/{orderId}/confirm")
    public String getOrderConfirmationForm(@PathVariable("orderId") Integer orderId,
                                           @RequestParam("isVisa") boolean isVisa,
                                           @RequestParam("isMasterCard") boolean isMasterCard,
                                           @RequestParam("ownerId") Integer ownerId,
                                           @RequestParam("ownerName") String ownerName,
                                           @RequestParam("orderStatus") OrderStatus orderStatus,
                                           @RequestParam("message") String message,
                                           Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        ResourceBundle rb = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        try {
            PaymentSystem paymentSystem = isMasterCard ? PaymentSystem.MASTERCARD : (isVisa ? PaymentSystem.VISA : null);
            CreditCardConfirmationDto creditCardConfirmationDto = creditCardService.getNewCreditCardConfDto(isVisa, isMasterCard);
            model.addAttribute("creditCardConfDto", creditCardConfirmationDto);
            MoneyAccountConfirmationDto moneyAccountConfirmationDto = moneyAccountService.getNewMoneyAccountConfDto();
            model.addAttribute("moneyAccountConfDto", moneyAccountConfirmationDto);
            model.addAttribute("orderConfDto", new CreditCardOrderWithUserDto(orderId, orderStatus, message,
                    isVisa, isMasterCard, ownerId, ownerName));
            creditCardOrderService.prepareCreditCardOrderConfirmation();
            model.addAttribute("errors", new HashMap<String, String[]>());
        } catch (UnsafeOrderConfirmationException e) {
            model.addAttribute("orderPreparingMessage", rb.getString("verification.order.unsafe.confirmation"));
            return "showAllOrders";
        } catch (UnsafeMoneyAccCreationException e) {
            model.addAttribute("moneyAccCreationMessage", rb.getString("verification.moneyAccount.unsafe.creation"));
            return "showAllOrders";
        }

        return "orderConfirmationForm";
    }

    @PatchMapping("{orderId}/confirm")
    public String confirmOrder(@PathVariable("orderId") Integer orderId,
                               @RequestParam("ownerId") Integer ownerId,
                               @RequestParam("creditCardNum") Long creditCardNum,
                               @RequestParam("CVV") Integer cvv,
                               @RequestParam("expireDateString")String expireDateString,
                               @RequestParam("moneyAccountNum") Long moneyAccountNum,
                               @RequestParam("moneyAccountName") String moneyAccountName,
                               @RequestParam("isVisa") boolean isVisa,
                               @RequestParam("isMasterCard") boolean isMasterCard,
                               @RequestParam("ownerName") String ownerName,
                               @RequestParam("message") String message,
                               Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        ResourceBundle rb = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        ValidationVisitor validationVisitor = new ValidationVisitor(rb);
        Map<String, String[]> validationErrorsMap = validationVisitor.visitMoneyAccountName(moneyAccountName);

        if (!validationErrorsMap.isEmpty()) {
            model.addAttribute("creditCardConfDto", new CreditCardConfirmationDto(creditCardNum, cvv,
                    expireDateString, isVisa, isMasterCard));
            model.addAttribute("moneyAccountConfDto", new MoneyAccountConfirmationDto(moneyAccountNum, moneyAccountName));
            model.addAttribute("orderConfDto", new CreditCardOrderWithUserDto(orderId, OrderStatus.ON_CHECK, message,
                    isVisa, isMasterCard, ownerId, ownerName));
            model.addAttribute("errors", validationErrorsMap);
            return "orderConfirmationForm";
        }
        creditCardOrderService.confirmCreditCardOrder(orderId, ownerId, creditCardNum, cvv, expireDateString,
                moneyAccountNum, moneyAccountName, isVisa, isMasterCard);

        return "redirect:/app/order";
    }

    @PatchMapping("/cancelConfirmation")
    public String cancelOrderConfirmation (Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        creditCardOrderService.cancelOrderConfirmation();
        return "redirect:/app/order";
    }
}
