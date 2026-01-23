package com.obed.affilishop.controller;

import com.obed.affilishop.model.Product;
import com.obed.affilishop.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private final ProductRepository productRepository;

    // Injection du repository via le constructeur
    public HomeController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ================= PAGE D'ACCUEIL =================
    @GetMapping("/")
    public String home(Model model, Principal principal) {
        // Récupère tous les produits pour l'accueil
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        // Ajouter le username de l'utilisateur connecté (null si pas connecté)
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }

        return "index"; // Retourne index.html
    }

    // ================= PAGE PRODUITS AVEC RECHERCHE =================
    @GetMapping("/products")
    public String products(@RequestParam(required = false) String keyword,
                           Model model) {

        List<Product> products;

        if (keyword != null && !keyword.isEmpty()) {
            // Recherche par mot-clé (nom ou description)
            products = productRepository.searchByKeyword(keyword);
        } else {
            // Si pas de mot-clé, on affiche tous les produits
            products = productRepository.findAll();
        }

        model.addAttribute("products", products);
        return "products"; // Retourne products.html
    }
}
