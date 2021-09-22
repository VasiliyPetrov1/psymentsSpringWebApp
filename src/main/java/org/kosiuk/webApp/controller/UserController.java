package org.kosiuk.webApp.controller;

import org.kosiuk.webApp.dto.CreditCardOrderDto;
import org.kosiuk.webApp.dto.UserCreationDto;
import org.kosiuk.webApp.dto.UserEditionDto;
import org.kosiuk.webApp.dto.UserLimitedEditionDto;
import org.kosiuk.webApp.entity.User;
import org.kosiuk.webApp.exceptions.NotCompatibleRolesException;
import org.kosiuk.webApp.service.CreditCardOrderService;
import org.kosiuk.webApp.service.UserService;
import org.kosiuk.webApp.util.AuthUtil;
import org.kosiuk.webApp.util.visitor.ValidationVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
@RequestMapping("/app/user")
public class UserController {

    private final UserService userService;
    private final CreditCardOrderService creditCardOrderService;

    @Autowired
    public UserController(UserService userService, CreditCardOrderService creditCardOrderService) {
        this.userService = userService;
        this.creditCardOrderService = creditCardOrderService;
    }

    @GetMapping("/new")
    public String getUserCreationForm(@ModelAttribute("userCreateDto") UserCreationDto userCreationDto,
                                      Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        model.addAttribute("errors", new HashMap<String, String[]>());
        return "newUser";
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createUser(@ModelAttribute("userCreateDto") UserCreationDto userCreationDto,
                             Model model){
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        ResourceBundle rb = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        ValidationVisitor validationVisitor = new ValidationVisitor(rb);
        Map<String, String[]> validationErrorsMap = (Map<String, String[]>) userCreationDto.accept(validationVisitor);

        if(!validationErrorsMap.isEmpty()) {
            model.addAttribute("userCreationDto", userCreationDto);
            model.addAttribute("errors", validationErrorsMap);
            return "newUser";
        }

        model.addAttribute("errors", new HashMap<String, String[]>());
        try {
            userService.createUser(userCreationDto);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("createUserMessage", rb.getString("verification.user.username.duplicate"));
            model.addAttribute("userCreationDto", userCreationDto);
            return "newUser";
        } catch (NotCompatibleRolesException notCompatibleRolesException) {
            model.addAttribute("createUserMessage", notCompatibleRolesException.getMessage() +
                    rb.getString("verification.role.notCompatible"));
            model.addAttribute("userCreationDto", userCreationDto);
            return "newUser";
        }

        return "redirect:/app/user";
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showAllUsers(Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        model.addAttribute("users", userService.getAllUsers());
        return "showAllUsers";
    }

    @GetMapping("{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getUserEditionForm(@PathVariable Integer userId, Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        model.addAttribute("errors", new HashMap<String, String[]>());
        model.addAttribute("userEditionDto", userService.convertUserToDTO(userService.getUserById(userId)));
        return "editUsers";
    }

    @PatchMapping("{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateUser(@ModelAttribute("userEditionDto") UserEditionDto userEditionDto,
                             Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        ResourceBundle rb = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        ValidationVisitor validationVisitor = new ValidationVisitor(rb);
        Map<String, String[]> validationErrorsMap = (Map<String, String[]>) userEditionDto.accept(validationVisitor);

        if(!validationErrorsMap.isEmpty()) {
            model.addAttribute("userEditionDto", userEditionDto);
            model.addAttribute("errors", validationErrorsMap);
            return "editUsers";
        }

        model.addAttribute("errors", new HashMap<String, String[]>());

        try {
            userService.updateUser(userEditionDto);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("updateUserMessage", rb.getString("verification.user.username.duplicate"));
            model.addAttribute("userEditionDto", userEditionDto);
            return "editUsers";
        } catch (NotCompatibleRolesException notCompatibleRolesException) {
            model.addAttribute("updateUserMessage", notCompatibleRolesException.getMessage() +
                    rb.getString("verification.role.notCompatible"));
            model.addAttribute("userEditionDto", userEditionDto);
            return "editUsers";
        }

        return "redirect:/app/user";
    }

    @PatchMapping("{userId}/lim")
    public String updateUserLimited(@PathVariable Integer userId,
                                    @ModelAttribute("userLimEditionDto")UserLimitedEditionDto userLimEditionDto,
                                    Model model){
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        ResourceBundle rb = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
        ValidationVisitor validationVisitor = new ValidationVisitor(rb);
        Map<String, String[]> validationErrorsMap = (Map<String, String[]>) userLimEditionDto.accept(validationVisitor);

        User user = userService.getUserById(userLimEditionDto.getId());

        if(!validationErrorsMap.isEmpty()) {
            model.addAttribute("userLimEditionDto", userLimEditionDto);
            model.addAttribute("creditCardOrderDtos", creditCardOrderService.getAllUsersOrderDtos(user));
            model.addAttribute("newOrderCreationDto", new CreditCardOrderDto());
            model.addAttribute("errors", validationErrorsMap);
            return "personalRoom";
        }

        model.addAttribute("errors", new HashMap<String, String[]>());

        try {
            userService.updateUserLimited(userLimEditionDto);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("updateUserMessage", rb.getString("verification.user.username.duplicate"));
            model.addAttribute("userLimEditionDto", userLimEditionDto);
            model.addAttribute("creditCardOrderDtos", creditCardOrderService.getAllUsersOrderDtos(user));
            model.addAttribute("newOrderCreationDto", new CreditCardOrderDto());
            return "personalRoom";
        }

        model.addAttribute("userLimEditionDto", userLimEditionDto);
        return "redirect:/app/personalRoom/" + userId;
    }

    @DeleteMapping("{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser(@PathVariable Integer userId,
                             Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        userService.deleteUser(userId);
        return "redirect:/app/user";
    }

}
