package com.obed.affilishop.controller;

import com.obed.affilishop.model.User;
import com.obed.affilishop.repository.UserRepository;
import com.obed.affilishop.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private EmailService emailService; // 🔑 Nou itilize EmailService olye de JavaMailSender dirèk

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Afficher la page d'inscription
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User()); // pour binder le formulaire
        return "register"; // correspond à register.html
    }

    // Traiter l'inscription
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        // Normaliser inputs
        username = username.trim();
        email = email.trim().toLowerCase();

        // Vérifier correspondance des mots de passe
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas");
            return "register";
        }

        // Vérifier si l'utilisateur existe déjà
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Ce nom d'utilisateur existe déjà");
            return "register";
        }

        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Cet email existe déjà");
            return "register";
        }

        // Créer et sauvegarder l'utilisateur
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // encodage sécurisé
        user.setRole("USER"); // rôle par défaut

        try {
            userRepository.save(user);

            // Envoyer un email de bienvenue via EmailService
            emailService.sendWelcomeEmail(email, username);

        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("error", "Nom d'utilisateur ou email déjà utilisé");
            return "register";
        } catch (Exception ex) {
            logger.error("Erreur lors de l'envoi d'email à {}: {}", email, ex.getMessage(), ex);
            model.addAttribute("error", "Compte créé mais impossible d'envoyer l'email de confirmation.");
            return "register";
        }

        // Message de succès — rester sur la page d'inscription
        model.addAttribute("success", "Compte créé avec succès ! Un email de confirmation a été envoyé.");
        return "register";
    }
}
