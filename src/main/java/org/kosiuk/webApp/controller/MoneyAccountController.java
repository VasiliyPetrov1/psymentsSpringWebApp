package org.kosiuk.webApp.controller;

import org.kosiuk.webApp.dto.MoneyAccountConfirmationDto;
import org.kosiuk.webApp.dto.MoneyAccountDto;
import org.kosiuk.webApp.dto.MoneyAccountWithUserDto;
import org.kosiuk.webApp.entity.MoneyAccount;
import org.kosiuk.webApp.entity.User;
import org.kosiuk.webApp.exceptions.UnsafeMoneyAccCreationException;
import org.kosiuk.webApp.service.MoneyAccountService;
import org.kosiuk.webApp.service.UserService;
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

@Controller
@RequestMapping("app/moneyAccount")
public class MoneyAccountController {

    private final MoneyAccountService moneyAccountService;
    private final UserService userService;
    @Value("${application.moneyAccountPageSize}")
    private int pageSize;

    @Autowired
    public MoneyAccountController(MoneyAccountService moneyAccountService, UserService userService) {
        this.moneyAccountService = moneyAccountService;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    public String showAllMoneyAccounts(Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        return showAllMoneyAccountsPage(1, "none", model);

    }

    @GetMapping("/page/{pageNumber}")
    public String showAllMoneyAccountsPage(@PathVariable("pageNumber") Integer pageNumber,
                                           @RequestParam("sortParameter") String sortParameter,
                                           Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);

        Page<MoneyAccount> page = moneyAccountService.getAllMoneyAccountsPage(pageNumber, sortParameter);
        List<MoneyAccount> moneyAccounts = page.getContent();

        model.addAttribute("curPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        List<MoneyAccountWithUserDto> moneyAccountDtos = moneyAccountService.
                convertMoneyAccountsToMoneyAccountDtosWithUser(moneyAccounts);
        model.addAttribute("moneyAccountDtos", moneyAccountDtos);

        model.addAttribute("sortParameter", sortParameter);

        return "showAllMoneyAccounts";
    }

    @GetMapping("/ofUser/{userId}")
    public String showAllUsersMoneyAccounts(@PathVariable("userId") Integer userId, Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        return showAllUsersMoneyAccountsPage(userId, 1,"none", model);

    }

    @GetMapping("/ofUser/{userId}/page/{pageNumber}")
    public String showAllUsersMoneyAccountsPage(@PathVariable("userId") Integer userId,
                                                @PathVariable("pageNumber") Integer pageNumber,
                                                @RequestParam("sortParameter") String sortParameter,
                                                Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);

        User user = userService.getUserById(userId);
        List<MoneyAccountDto> moneyAccountDtos = moneyAccountService.getAllSortedUsersMoneyAccountDtos(user, sortParameter);

        int totalItems = moneyAccountDtos.size();

        int totalPages;
        if ((totalItems % pageSize) != 0) {
            totalPages = moneyAccountDtos.size() / pageSize + 1;
        } else {
            totalPages = moneyAccountDtos.size() / pageSize;
        }

        int lastElNumber;
        if (totalItems == 0) {
            lastElNumber = 0;
        } else if ((totalItems % pageSize) != 0 && pageNumber == totalPages) {
            lastElNumber = (pageSize * (pageNumber - 1)) + totalItems % pageSize;
        } else {
            lastElNumber = pageSize * pageNumber;
        }

        model.addAttribute("curPage", pageNumber);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("moneyAccountDtos", moneyAccountDtos.subList(pageSize * (pageNumber - 1),
                lastElNumber));

        model.addAttribute("userId", userId);
        model.addAttribute("sortParameter", sortParameter);

        return "showAllUsersMoneyAccounts";
    }

    @GetMapping("{moneyAccId}")
    public String showMoneyAccountByid(@PathVariable("moneyAccId") Integer moneyAccId, Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);

        MoneyAccountDto moneyAccountDto = moneyAccountService.getMoneyAccountDtoById(moneyAccId);

        model.addAttribute("moneyAccountDto", moneyAccountDto);

        return "showMoneyAccount";
    }

    @GetMapping("getCreationForm")
    public String getMoneyAccountCreationForm(Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        MoneyAccountConfirmationDto moneyAccountConfDto;
        try {
            moneyAccountConfDto = moneyAccountService.getNewMoneyAccountConfDto();
            model.addAttribute("moneyAccountConfDto", moneyAccountConfDto);
        } catch (UnsafeMoneyAccCreationException e) {
            model.addAttribute("moneyAccCreationMessage", "Unsafe creation. Other ADMIN " +
                    "is trying to create some money account now. Try later");
            return "showAllMoneyAccounts";
        }

        return "newMoneyAccount";

    }

    @PostMapping()
    public String createMoneyAccount(@RequestParam("moneyAccountNum") Long moneyAccountNum,
                                     @RequestParam("moneyAccountName") String moneyAccountName,
                                     Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        moneyAccountService.createMoneyAccount(moneyAccountNum, moneyAccountName);
        return "showAllMoneyAccounts";

    }

    @DeleteMapping("{moneyAccId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteMoneyAccount(@PathVariable("moneyAccId") Integer moneyAccId,
                                     Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        moneyAccountService.deleteMoneyAccount(moneyAccId);
        return "redirect:/app/moneyAccount";
    }

    @PatchMapping("{moneyAccId}/block")
    public String blockMoneyAccount (@PathVariable("moneyAccId") Integer moneyAccId,
                                     Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        moneyAccountService.blockMoneyAccount(moneyAccId);
        return "redirect:/app/moneyAccount/" + moneyAccId;
    }

    @PatchMapping("{moneyAccId}/askToUnlock")
    public String askToUnlockMoneyAccount(@PathVariable("moneyAccId") Integer moneyAccId,
                                          Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        moneyAccountService.askToUnlockMoneyAccount(moneyAccId);
        return "redirect:/app/moneyAccount/" + moneyAccId;
    }

    @PatchMapping("{moneyAccId}/unlock")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String unlockMoneyAccount(@PathVariable("moneyAccId") Integer moneyAccId, Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        moneyAccountService.unlockMoneyAccount(moneyAccId);
        return "redirect:/app/moneyAccount";
    }

    @PatchMapping("cancelCreation")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String cancelCreation(Model model) {
        AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
        moneyAccountService.cancelCreation();
        return "redirect:/app/moneyAccount";
    }

}
