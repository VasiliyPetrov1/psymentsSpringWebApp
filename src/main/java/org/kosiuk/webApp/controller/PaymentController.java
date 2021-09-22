package org.kosiuk.webApp.controller;

import org.kosiuk.webApp.dto.*;
import org.kosiuk.webApp.entity.Payment;
import org.kosiuk.webApp.exceptions.*;
import org.kosiuk.webApp.service.PaymentService;
import org.kosiuk.webApp.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
        return "toCardPaymentForm";
    }

    @GetMapping("/getToCardConfPaymentForm")
    public String checkToCardPayment(@ModelAttribute("cardPaymentPrepDto")CardPaymentPreparationDto cardPaymentPrepDto,
                                     @ModelAttribute("cardPaymentConfDto") CardPaymentConfirmationDto cardPaymentConfirmationDto,
                                     Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);

        try {
            CardPaymentConfirmationDto cardPaymentConfDto = paymentService.prepareToCardPayment(cardPaymentPrepDto);
            cardPaymentConfirmationDto.setSenderMoneyAccountId(cardPaymentConfDto.getSenderMoneyAccountId());
            cardPaymentConfirmationDto.setReceiverCreditCardNumber(cardPaymentConfDto.getReceiverCreditCardNumber());
            cardPaymentConfirmationDto.setReceiverAccountName(cardPaymentConfDto.getReceiverAccountName());
            cardPaymentConfirmationDto.setPayedSum(cardPaymentConfDto.getPayedSum());
            cardPaymentConfirmationDto.setAssignment(cardPaymentConfDto.getAssignment());
            cardPaymentConfirmationDto.setPaymentComission(cardPaymentConfDto.getPaymentComission());
        } catch (NoCreditCardByNumberException e) {
            model.addAttribute("paymentPrepMessage", "There's no card with given number");
        } catch (ToOwnRequisitePaymentException e) {
            model.addAttribute("paymentPrepMessage", "Payments on your own account are forbidden");
        } catch (BlockedAccountException e) {
            model.addAttribute("paymentPrepMessage", e.getMessage());
        } catch (NotEnoughMoneyOnAccountException e) {
            model.addAttribute("paymentPrepMessage", "You dont have enough money for this payment");
            cardPaymentConfirmationDto.setPayedSum(e.getPayedSum());
            cardPaymentConfirmationDto.setPaymentComission(e.getPaymentComission());
        }
        return "toCardPaymentConfForm";

    }

    @PostMapping("toCard")
    public String confirmToCardPayment(@ModelAttribute("cardPaymentConfDto") CardPaymentConfirmationDto cardPaymentConfDto,
                                       Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        paymentService.confirmToCardPayment(cardPaymentConfDto);
        return "redirect:/app/payment/" + cardPaymentConfDto.getSenderMoneyAccountId();
    }

    @GetMapping("/getToAccountPaymentForm/{senderAccId}")
    public String prepareToMoneyAccountPaymentForm(@PathVariable("senderAccId") Integer senderAccId,
                                               @ModelAttribute("moneyAccPaymentPrepDto")
                                                       MoneyAccPaymentPreparationDto moneyAccPaymentPrepDto,
                                               Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        moneyAccPaymentPrepDto.setSenderMoneyAccountId(senderAccId);
        return "toAccountPaymentForm";

    }

    @GetMapping("/getToAccountConfPaymentForm")
    public String prepareToMoneyAccountPayment(@ModelAttribute("moneyAccPaymentPrepDto") MoneyAccPaymentPreparationDto moneyAccPaymentPrepDto,
                                            @ModelAttribute("moneyAccPaymentConfDto") MoneyAccPaymentConfirmationDto moneyAccPaymentConfirmationDto,
                                            Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        try {
            MoneyAccPaymentConfirmationDto moneyAccPaymentConfDto =
                    paymentService.prepareToMoneyAccountPayment(moneyAccPaymentPrepDto);
            moneyAccPaymentConfirmationDto.setSenderMoneyAccountId(moneyAccPaymentConfDto.getSenderMoneyAccountId());
            moneyAccPaymentConfirmationDto.setReceiverMoneyAccountNumber(moneyAccPaymentConfDto.getReceiverMoneyAccountNumber());
            moneyAccPaymentConfirmationDto.setReceiverAccountName(moneyAccPaymentConfDto.getReceiverAccountName());
            moneyAccPaymentConfirmationDto.setPayedSum(moneyAccPaymentConfDto.getPayedSum());
            moneyAccPaymentConfirmationDto.setAssignment(moneyAccPaymentConfDto.getAssignment());
            moneyAccPaymentConfirmationDto.setPaymentComission(moneyAccPaymentConfDto.getPaymentComission());
        } catch (NoMoneyAccountByNumberException e) {
            model.addAttribute("moneyAccPrepMessage", "There's no money account with given number");
        } catch (ToOwnRequisitePaymentException e) {
            model.addAttribute("moneyAccPrepMessage", "Payments on your own account are forbidden");
        } catch (BlockedAccountException e) {
            model.addAttribute("moneyAccPrepMessage", e.getMessage());
        } catch (NotEnoughMoneyOnAccountException e) {
            model.addAttribute("moneyAccPrepMessage", "You dont have enough money for this payment");
            moneyAccPaymentConfirmationDto.setPayedSum(e.getPayedSum());
            moneyAccPaymentConfirmationDto.setPaymentComission(e.getPaymentComission());
        }

        return "toAccountPaymentConfForm";
    }

    @PostMapping("toAccount")
    public String confirmToMoneyAccountPayment(@ModelAttribute("moneyAccPaymentConfDto")
                                               MoneyAccPaymentConfirmationDto moneyAccPaymentConfDto,
                                               Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        paymentService.confirmToMoneyAccountPayment(moneyAccPaymentConfDto);

        return "redirect:/app/payment/" + moneyAccPaymentConfDto.getSenderMoneyAccountId();
    }

    @GetMapping("/paymentDetails/{paymentNum}")
    public String showPaymentDetails(@PathVariable("paymentNum") Long paymentNum,
                                     Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        try {
            PaymentDetailsDto paymentDetailsDto = paymentService.getPaymentDetails(paymentNum);
            model.addAttribute("paymentDetailsDto", paymentDetailsDto);
        } catch (NoSuchElementException | NullPointerException e) {
            model.addAttribute("paymentDetailsMessage", "There's no payment with given number.");
        }
        return "showPaymentDetails";
    }

}
