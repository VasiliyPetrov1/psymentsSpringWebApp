package org.kosiuk.webApp.controller;

import org.kosiuk.webApp.dto.CreditCardDto;
import org.kosiuk.webApp.dto.PaymentDto;
import org.kosiuk.webApp.entity.CreditCard;
import org.kosiuk.webApp.entity.Payment;
import org.kosiuk.webApp.entity.User;
import org.kosiuk.webApp.service.CreditCardService;
import org.kosiuk.webApp.service.UserService;
import org.kosiuk.webApp.util.AuthUtil;
import org.kosiuk.webApp.util.visitor.ValidationVisitor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
@RequestMapping("/app/creditCard")
public class СreditCardController {

    private final CreditCardService creditCardService;
    private final UserService userService;

    public СreditCardController(CreditCardService creditCardService, UserService userService) {
        this.creditCardService = creditCardService;
        this.userService = userService;
    }

    @GetMapping("/ofUser/{userId}")
    public String showAllUsersCreditCards(@PathVariable("userId") Integer userId,
                                          Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        return showAllUsersCreditCardsPage(userId, 1, model);
    }

    @GetMapping("/ofUser/{userId}/page/{pageNumber}")
    public String showAllUsersCreditCardsPage(@PathVariable("userId") Integer userId,
                                              @PathVariable("pageNumber") Integer pageNumber,
                                              Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);

        User user = userService.getUserById(userId);
        Page<CreditCard> page = creditCardService.getAllUsersCreditCardsPage(pageNumber, user);

        model.addAttribute("curPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        List<CreditCard> creditCards = page.getContent();

        List<CreditCardDto> creditCardDtos = creditCardService.
                convertCreditCardsToCreditCardDtos(creditCards);
        model.addAttribute("creditCardDtos", creditCardDtos);

        model.addAttribute("userId", userId);

        return "showAllCreditCards";
    }

    @GetMapping("/putMoney/{cardId}")
    public String getMoneyPutForm(@PathVariable("cardId") Integer cardId,
                                  Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);

        model.addAttribute("cardId", cardId);
        model.addAttribute("errors", new HashMap<String, String[]>());
        return "moneyPutForm";

    }

    @PatchMapping("/putMoney/{cardId}")
    public String putMoney(@PathVariable("cardId") Integer cardId,
                           @RequestParam("sumString") String sumString,
                           Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        ResourceBundle rb = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        ValidationVisitor validationVisitor = new ValidationVisitor(rb);
        Map<String, String[]> validationErrorsMap = validationVisitor.visitSumString(sumString);

        if (!validationErrorsMap.isEmpty()) {
            model.addAttribute("sumString", sumString);
            model.addAttribute("cardId", cardId);
            model.addAttribute("errors", validationErrorsMap);
            return "moneyPutForm";
        }

        Integer ownerId = creditCardService.putMoney(cardId, sumString).getUser().getId();

        return "redirect:/app/creditCard/ofUser/" + ownerId;

    }
}
