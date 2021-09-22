package org.kosiuk.webApp.util;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.Set;
import java.util.stream.Collectors;

public class AuthUtil {

    public static void addRolesToModel(Authentication authentication, Model model) {
        Set<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet());
        model.addAttribute("visitorRoles", roles);
    }

}
