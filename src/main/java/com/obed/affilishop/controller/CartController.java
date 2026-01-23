package com.obed.affilishop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @GetMapping("/cart")
    public String viewCart(Principal principal, Model model) {
        String username = (principal != null) ? principal.getName() : "invité";
        model.addAttribute("username", username);

        // Pour l'instant, panier vide (implémentation future : lier panier à l'utilisateur)
        List<Object> items = new ArrayList<>();
        model.addAttribute("items", items);

        return "cart";
    }
}
