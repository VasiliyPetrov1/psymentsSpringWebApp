package org.kosiuk.webApp.controller;

import org.kosiuk.webApp.dto.*;
import org.kosiuk.webApp.entity.Payment;
import org.kosiuk.webApp.exceptions.*;
import org.kosiuk.webApp.service.PaymentService;
import org.kosiuk.webApp.util.AuthUtil;
import org.kosiuk.webApp.util.visitor.ValidationVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/app/payment")
public class PaymentController {


    private final PaymentService paymentService;
    @Value("${application.halfPaymentsPageSize}")
    private int pageSize;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showAllPayments(Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        return showAllPaymentsPage(1, "none", model);

    }

    @GetMapping("/page/{pageNumber}")
    public String showAllPaymentsPage(@PathVariable("pageNumber") Integer pageNumber,
                                      @RequestParam("sortParameter") String sortParameter,
                                      Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);

        Page<Payment> page = paymentService.getAllPaymentsPage(pageNumber, sortParameter);
        List<Payment> payments = page.getContent();

        model.addAttribute("curPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        List<PaymentDto> allPaymentDtos = paymentService.
                convertPaymentsToPaymentDtos(payments);
        model.addAttribute("allPaymentDtos", allPaymentDtos);

        model.addAttribute("sortParameter", sortParameter);

        return "showAllPayments";
    }

    @GetMapping("{moneyAccId}/page/{sentPageNumber}/{receivedPageNumber}")
    public String showAllPaymentsOnAccountPage(@PathVariable("moneyAccId") Integer moneyAccId,
                                               @PathVariable("sentPageNumber") Integer sentPageNumber,
                                               @PathVariable("receivedPageNumber") Integer receivedPageNumber,
                                               @RequestParam("sortParameter") String sortParameter,
                                               Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);

        List<PaymentDto> sentPaymentDtos = paymentService.getAllSortedSentByAccountPaymentDtos(moneyAccId, sortParameter);
        List<PaymentDto> receivedPaymentDtos =
                paymentService.getAllSortedReceivedByAccountPaymentDtos(moneyAccId, sortParameter);

        int totalSentItems = sentPaymentDtos.size();

        int totalSentPages;
        if ((totalSentItems % pageSize) != 0) {
            totalSentPages = totalSentItems / pageSize + 1;
        } else {
            totalSentPages = totalSentItems / pageSize;
        }

        int lastSentElNumber;
        if (totalSentItems == 0) {
            lastSentElNumber = 0;
        } else if ((totalSentItems % pageSize) != 0 && sentPageNumber == totalSentPages) {
            lastSentElNumber = (pageSize * (sentPageNumber - 1)) + totalSentItems % pageSize;
        } else {
            lastSentElNumber = pageSize * sentPageNumber;
        }

        int totalReceivedItems = receivedPaymentDtos.size();

        int totalReceivedPages;
        if ((totalReceivedItems % pageSize) != 0) {
            totalReceivedPages = receivedPaymentDtos.size() / pageSize + 1;
        } else {
            totalReceivedPages = receivedPaymentDtos.size() / pageSize;
        }

        int lastReceivedElNumber;
        if (totalReceivedItems == 0) {
            lastReceivedElNumber = 0;
        } else if ((totalReceivedItems % pageSize) != 0 && receivedPageNumber == totalReceivedPages) {
            lastReceivedElNumber = (pageSize * (receivedPageNumber - 1)) + totalReceivedItems % pageSize;
        } else {
            lastReceivedElNumber = pageSize * receivedPageNumber;
        }

        model.addAttribute("curSentPage", sentPageNumber);
        model.addAttribute("totalSentPages", totalSentPages);
        model.addAttribute("totalSentItems", totalSentItems);
        model.addAttribute("sentPaymentDtos", sentPaymentDtos.subList(pageSize * (sentPageNumber - 1),
                lastSentElNumber));

        model.addAttribute("curReceivedPage", receivedPageNumber);
        model.addAttribute("totalReceivedPages", totalReceivedPages);
        model.addAttribute("totalReceivedItems", totalReceivedItems);
        model.addAttribute("receivedPaymentDtos", receivedPaymentDtos.subList(pageSize * (receivedPageNumber - 1),
                lastReceivedElNumber));

        model.addAttribute("moneyAccId", moneyAccId);
        model.addAttribute("sortParameter", sortParameter);

        return "showAllPaymentsOnAccount";

    }

    @GetMapping("{moneyAccId}")
    public String showAllPaymentsOnAccount(@PathVariable("moneyAccId") Integer moneyAccId, Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        return showAllPaymentsOnAccountPage(moneyAccId, 1,1, "none", model);

    }

    @GetMapping("/getToCardPaymentForm/{senderAccId}")
    public String getToCardPaymentForm(@PathVariable("senderAccId") Integer senderAccId,
                                       @ModelAttribute("cardPaymentPrepDto")CardPaymentPreparationDto cardPaymentPrepDto,
                                       Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        cardPaymentPrepDto.setSenderMoneyAccountId(senderAccId);
        model.addAttribute("errors", new HashMap<String, String[]>());
        return "toCardPaymentForm";
    }

    @GetMapping("/getToCardConfPaymentForm")
    public String prepareToCardPayment(@ModelAttribute("cardPaymentPrepDto")CardPaymentPreparationDto cardPaymentPrepDto,
                                       @ModelAttribute("cardPaymentConfDto") CardPaymentConfirmationDto cardPaymentConfirmationDto,
                                       Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        ResourceBundle rb = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        ValidationVisitor validationVisitor = new ValidationVisitor(rb);
        Map<String, String[]> validationErrorsMap = (Map<String, String[]>) cardPaymentPrepDto.accept(validationVisitor);

        if(!validationErrorsMap.isEmpty()) {
            model.addAttribute("cardPaymentPrepDto", cardPaymentPrepDto);
            model.addAttribute("errors", validationErrorsMap);
            return "toCardPaymentForm";
        }

        try {
            CardPaymentConfirmationDto cardPaymentConfDto = paymentService.prepareToCardPayment(cardPaymentPrepDto, rb);
            cardPaymentConfirmationDto.setSenderMoneyAccountId(cardPaymentConfDto.getSenderMoneyAccountId());
            cardPaymentConfirmationDto.setReceiverCreditCardNumber(cardPaymentConfDto.getReceiverCreditCardNumber());
            cardPaymentConfirmationDto.setReceiverAccountName(cardPaymentConfDto.getReceiverAccountName());
            cardPaymentConfirmationDto.setPayedSumString(cardPaymentConfDto.getPayedSumString());
            cardPaymentConfirmationDto.setAssignment(cardPaymentConfDto.getAssignment());
            cardPaymentConfirmationDto.setMovedSumString(cardPaymentConfDto.getMovedSumString());
            cardPaymentConfirmationDto.setPaymentComissionString(cardPaymentConfDto.getPaymentComissionString());
            cardPaymentConfirmationDto.setReceiverMoneyAccountId(cardPaymentConfDto.getReceiverMoneyAccountId());
            cardPaymentConfirmationDto.setPaymentNumber(cardPaymentConfDto.getPaymentNumber());
        } catch (NoCreditCardByNumberException e) {
            model.addAttribute("paymentPrepMessage", rb.getString("verification.payment.noCard.byNumber"));
            model.addAttribute("errors", new HashMap<String, String[]>());
            return "toCardPaymentForm";
        } catch (ToOwnRequisitePaymentException e) {
            model.addAttribute("paymentPrepMessage", rb.getString("verification.payment.onOwnMoneyAcc"));
            model.addAttribute("errors", new HashMap<String, String[]>());
            return "toCardPaymentForm";
        } catch (BlockedAccountException e) {
            model.addAttribute("paymentPrepMessage", e.getMessage());
            model.addAttribute("errors", new HashMap<String, String[]>());
            return "toCardPaymentForm";
        } catch (NotEnoughMoneyOnAccountException e) {
            model.addAttribute("paymentPrepMessage", rb.getString("verification.payment.notEnoughMoney"));
            model.addAttribute("notEnoughSumString", e.getPayedSumString());
            model.addAttribute("notEnoughComissionString", e.getPaymentComissionString());
            model.addAttribute("errors", new HashMap<String, String[]>());
            return "toCardPaymentForm";
        }

        return "toCardPaymentConfForm";

    }

    @GetMapping("/getToAccountPaymentForm/{senderAccId}")
    public String getToMoneyAccountPaymentForm(@PathVariable("senderAccId") Integer senderAccId,
                                               @ModelAttribute("moneyAccPaymentPrepDto")
                                                       MoneyAccPaymentPreparationDto moneyAccPaymentPrepDto,
                                               Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        moneyAccPaymentPrepDto.setSenderMoneyAccountId(senderAccId);
        model.addAttribute("errors", new HashMap<String, String[]>());
        return "toAccountPaymentForm";

    }

    @GetMapping("/getToAccountConfPaymentForm")
    public String prepareToMoneyAccountPayment(@ModelAttribute("moneyAccPaymentPrepDto") MoneyAccPaymentPreparationDto moneyAccPaymentPrepDto,
                                            @ModelAttribute("moneyAccPaymentConfDto") MoneyAccPaymentConfirmationDto moneyAccPaymentConfirmationDto,
                                            Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        ResourceBundle rb = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        ValidationVisitor validationVisitor = new ValidationVisitor(rb);
        Map<String, String[]> validationErrorsMap = (Map<String, String[]>) moneyAccPaymentPrepDto.accept(validationVisitor);

        if(!validationErrorsMap.isEmpty()) {
            model.addAttribute("moneyAccPaymentPrepDto", moneyAccPaymentPrepDto);
            model.addAttribute("errors", validationErrorsMap);
            return "toAccountPaymentForm";
        }
        try {
            MoneyAccPaymentConfirmationDto moneyAccPaymentConfDto =
                    paymentService.prepareToMoneyAccountPayment(moneyAccPaymentPrepDto, rb);
            moneyAccPaymentConfirmationDto.setSenderMoneyAccountId(moneyAccPaymentConfDto.getSenderMoneyAccountId());
            moneyAccPaymentConfirmationDto.setReceiverMoneyAccountNumber(moneyAccPaymentConfDto.getReceiverMoneyAccountNumber());
            moneyAccPaymentConfirmationDto.setReceiverAccountName(moneyAccPaymentConfDto.getReceiverAccountName());
            moneyAccPaymentConfirmationDto.setPayedSumString(moneyAccPaymentConfDto.getPayedSumString());
            moneyAccPaymentConfirmationDto.setAssignment(moneyAccPaymentConfDto.getAssignment());
            moneyAccPaymentConfirmationDto.setMovedSumString(moneyAccPaymentConfDto.getMovedSumString());
            moneyAccPaymentConfirmationDto.setPaymentComissionString(moneyAccPaymentConfDto.getPaymentComissionString());
            moneyAccPaymentConfirmationDto.setReceiverMoneyAccountId(moneyAccPaymentConfDto.getReceiverMoneyAccountId());
            moneyAccPaymentConfirmationDto.setPaymentNumber(moneyAccPaymentConfDto.getPaymentNumber());
        } catch (NoMoneyAccountByNumberException e) {
            model.addAttribute("moneyAccPrepMessage", rb.getString("verification.payment.noMonAcc.byNumber"));
            model.addAttribute("errors", new HashMap<String, String[]>());
            return "toAccountPaymentForm";
        } catch (ToOwnRequisitePaymentException e) {
            model.addAttribute("moneyAccPrepMessage", rb.getString("verification.payment.onOwnMoneyAcc"));
            model.addAttribute("errors", new HashMap<String, String[]>());
            return "toAccountPaymentForm";
        } catch (BlockedAccountException e) {
            model.addAttribute("moneyAccPrepMessage", e.getMessage());
            model.addAttribute("errors", new HashMap<String, String[]>());
            return "toAccountPaymentForm";
        } catch (NotEnoughMoneyOnAccountException e) {
            model.addAttribute("moneyAccPrepMessage", rb.getString("verification.payment.notEnoughMoney"));
            model.addAttribute("notEnoughSumString", e.getPayedSumString());
            model.addAttribute("notEnoughComissionString", e.getPaymentComissionString());
            model.addAttribute("errors", new HashMap<String, String[]>());
            return "toAccountPaymentForm";
        }

        return "toAccountPaymentConfForm";
    }

    @PutMapping("toCard")
    public String confirmToCardPayment(@ModelAttribute("cardPaymentConfDto") CardPaymentConfirmationDto cardPaymentConfDto,
                                       Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        paymentService.confirmToCardPayment(cardPaymentConfDto);
        return "redirect:/app/payment/" + cardPaymentConfDto.getSenderMoneyAccountId();
    }

    @PutMapping("toAccount")
    public String confirmToMoneyAccountPayment(@ModelAttribute("moneyAccPaymentConfDto")
                                               MoneyAccPaymentConfirmationDto moneyAccPaymentConfDto,
                                               Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        paymentService.confirmToMoneyAccountPayment(moneyAccPaymentConfDto);

        return "redirect:/app/payment/" + moneyAccPaymentConfDto.getSenderMoneyAccountId();
    }

    @DeleteMapping()
    public String cancelPayment(@RequestParam("senderMoneyAccId") Integer senderMoneyAccId,
                                @RequestParam("paymentNumber") Long paymentNumber,
                                @RequestParam("payedSumString") String payedSumString,
                                Model model) {
        PaymentCancellationDto paymentCancellationDto = new PaymentCancellationDto(senderMoneyAccId, paymentNumber,
                payedSumString);

        paymentService.cancelPayment(paymentCancellationDto);

        return "redirect:/app";
    }

    @GetMapping("/paymentDetails/{paymentNum}")
    public String showPaymentDetails(@PathVariable("paymentNum") Long paymentNum,
                                     Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        ResourceBundle rb = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        try {
            PaymentDetailsDto paymentDetailsDto = paymentService.getPaymentDetails(paymentNum, rb);
            model.addAttribute("paymentDetailsDto", paymentDetailsDto);
        } catch (NoSuchElementException | NullPointerException e) {
            model.addAttribute("paymentDetailsMessage", "There's no payment with given number.");
        }
        return "showPaymentDetails";
    }

}
