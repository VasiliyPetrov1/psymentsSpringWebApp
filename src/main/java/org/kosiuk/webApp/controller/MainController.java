package org.kosiuk.webApp.controller;

import org.kosiuk.webApp.util.AuthUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/app")
public class MainController {

        @GetMapping()
        public String showMainPage(Model model) {
            AuthUtil.addRolesToModel(SecurityContextHolder.getContext().getAuthentication(), model);
            return "home";
        }

}
