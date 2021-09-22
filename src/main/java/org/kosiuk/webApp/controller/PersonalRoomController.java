package org.kosiuk.webApp.controller;

import org.kosiuk.webApp.dto.CreditCardOrderDto;
import org.kosiuk.webApp.entity.User;
import org.kosiuk.webApp.service.CreditCardOrderService;
import org.kosiuk.webApp.service.UserService;
import org.kosiuk.webApp.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/app/personalRoom")
@PreAuthorize("isAuthenticated()")
public class   PersonalRoomController {

    private final UserService userService;
    private final CreditCardOrderService creditCardOrderService;

    @Autowired
    public PersonalRoomController(UserService userService, CreditCardOrderService creditCardOrderService) {
        this.userService = userService;
        this.creditCardOrderService = creditCardOrderService;
    }

    @GetMapping()
    public String showPersonalRoomWithoutGivenUser(Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "redirect:/app/personalRoom/" + user.getId();
    }

    @GetMapping("{userId}")
    public String showPersonalRoom (@PathVariable Integer userId,
                                    @ModelAttribute("newOrderCreationDto") CreditCardOrderDto newOrderCreationDto,
                                    Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        User user = userService.getUserById(userId);
        model.addAttribute("userLimEditionDto", userService.convertUserToLimDto(user));
        model.addAttribute("creditCardOrderDtos", creditCardOrderService.getAllUsersOrderDtos(user));
        model.addAttribute("errors", new HashMap<String, String[]>());
        return "personalRoom";
    }
}
