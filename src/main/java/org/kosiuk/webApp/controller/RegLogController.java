package org.kosiuk.webApp.controller;

import org.kosiuk.webApp.dto.UserRegistrationDto;
import org.kosiuk.webApp.service.UserService;
import org.kosiuk.webApp.util.AuthUtil;
import org.kosiuk.webApp.util.visitor.ValidationVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
@RequestMapping("/app")
public class RegLogController {

    private final UserService userService;

    @Autowired
    public RegLogController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String getRegistrationForm(@ModelAttribute("userRegDto") UserRegistrationDto userRegDto,
                                      Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        model.addAttribute("errors", new HashMap<String, String[]>());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("userRegDto") UserRegistrationDto userRegDto,
                               Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        ResourceBundle rb = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        ValidationVisitor validationVisitor = new ValidationVisitor(rb);
        Map<String, String[]> validationErrorsMap = (Map<String, String[]>) userRegDto.accept(validationVisitor);

        if(!validationErrorsMap.isEmpty()) {
            model.addAttribute("userRegDto", userRegDto);
            model.addAttribute("errors", validationErrorsMap);
            return "registration";
        }


        try {
            userService.registerUser(userRegDto);
            model.addAttribute("addUserMessage", rb.getString("validation.user.reg.successful"));
            return "login";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errors", validationErrorsMap);
            model.addAttribute("addUserMessage", rb.getString("verification.user.username.duplicate"));
            return "registration";
        }

    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "login";
    }

    @GetMapping("/loginError")
    public String showLoginError() {
        return "loginError";
    }

}
