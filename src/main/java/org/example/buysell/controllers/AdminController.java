package org.example.buysell.controllers;

import lombok.RequiredArgsConstructor;
import org.example.buysell.configuration.CustomUserDetails;
import org.example.buysell.models.User;
import org.example.buysell.models.enums.Role;
import org.example.buysell.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final UserService userService;

    @GetMapping("/admin")
    public String admin(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("users", userService.getUserList());
        model.addAttribute("user", userService.getAuthorisedUser(user));
        return "admin";
    }

    @PostMapping("/admin/user/ban/{id}")
    public String banUser(@PathVariable("id") Long id) {
        userService.banUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/user/unban/{id}")
    public String unbanUser(@PathVariable("id") Long id) {
        userService.unbanUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user/edit/{user}")
    public String editUser(@PathVariable("user") User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    @PostMapping("/admin/user/edit")
    public String editUser(@RequestParam("userId") User user, @RequestParam Map<String, String> form) {
        userService.changeUserRoles(user, form);
        return "redirect:/admin";
    }
}
