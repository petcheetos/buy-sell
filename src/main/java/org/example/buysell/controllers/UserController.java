package org.example.buysell.controllers;

import lombok.RequiredArgsConstructor;
import org.example.buysell.configuration.CustomUserDetails;
import org.example.buysell.models.User;
import org.example.buysell.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(Principal principal,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));

        if (error != null || logout != null) {
            Map<String, Object> requestParams = new HashMap<>();
            if (error != null) {
                requestParams.put("error", true);
            }
            if (logout != null) {
                requestParams.put("logout", true);
            }
            model.addAttribute("RequestParameters", requestParams);
        }

        return "login";
    }

    @GetMapping("/registration")
    public String registration(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("user", userService.getAuthorisedUser(user));
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с email: " + user.getEmail() + " уже существует");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/user/{id}")
    public String userInfo(@PathVariable("id") User user, Model model, @AuthenticationPrincipal CustomUserDetails principal) {
        if (Objects.equals(user.getId(), principal.getUser().getId())) {
            return profile(principal, model);
        }
        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        model.addAttribute("userByPrincipal", userService.getAuthorisedUser(principal));
        return "user-info";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CustomUserDetails principal, Model model) {
        model.addAttribute("user", userService.getAuthorisedUser(principal));
        return "profile";
    }
}
