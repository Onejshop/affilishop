package com.obed.affilishop.controller;

import com.obed.affilishop.model.Product;
import com.obed.affilishop.model.User;
import com.obed.affilishop.repository.ProductRepository;
import com.obed.affilishop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);

    @Autowired
    private JavaMailSender mailSender;

    public AdminDashboardController(ProductRepository productRepository,
                                    UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Produits
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("totalProducts", products.size());

        double averagePrice = products.stream()
                                      .mapToDouble(Product::getPrice)
                                      .average()
                                      .orElse(0.0);
        model.addAttribute("averagePrice", averagePrice);
        model.addAttribute("product", new Product());

        // ✅ Utilisateurs
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        // ✅ Statistiques pour le graphique
        int totalClicks = 1200;   // ⚠️ Exemple statique, à remplacer par vos données réelles
        int totalSignups = users.size();
        double totalRevenue = products.stream()
                                      .mapToDouble(Product::getPrice)
                                      .sum(); // ⚠️ Exemple: somme des prix comme revenu

        model.addAttribute("totalClicks", totalClicks);
        model.addAttribute("totalSignups", totalSignups);
        model.addAttribute("totalRevenue", totalRevenue);

        return "admin-dashboard";
    }

    // ================= AJOUT PRODUIT =================
    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute Product product,
                             BindingResult bindingResult,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "La description est trop longue (max 250 caractères) ou d'autres erreurs de validation.");
            return "redirect:/admin/dashboard";
        }

        if (!imageFile.isEmpty()) {
            String fileName = UUID.randomUUID() + "-" + imageFile.getOriginalFilename();
            Path uploadDir = Paths.get("uploads").toAbsolutePath();
            Files.createDirectories(uploadDir);

            Path destination = uploadDir.resolve(fileName);
            try (InputStream is = imageFile.getInputStream()) {
                Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
            }
            product.setImageUrl("/uploads/" + fileName);
        }

        try {
            productRepository.save(product);
            redirectAttributes.addFlashAttribute("success", "Produit ajouté avec succès.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout du produit.");
        }

        return "redirect:/admin/dashboard";
    }

    // ================= ENVOYER EMAIL =================
    @PostMapping("/send-email")
    public String sendEmail(@RequestParam String email, RedirectAttributes redirectAttributes) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Bienvenue sur AffiliShop !");
        message.setText("Merci pour votre inscription sur AffiliShop. Profitez de nos produits affiliés !");
        mailSender.send(message);
        redirectAttributes.addFlashAttribute("success", "Message envoyé.");
        return "redirect:/admin/dashboard";
    }

    // ================= SUPPRIMER UTILISATEUR =================
    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        logger.info("Request to delete user id={}", id);

        if (id == null || !userRepository.existsById(id)) {
            return "redirect:/admin/dashboard";
        }

        try {
            userRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Utilisateur supprimé.");
        } catch (Exception ex) {
            logger.error("Error deleting user id={}", id, ex);
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer l'utilisateur.");
        }

        return "redirect:/admin/dashboard";
    }

    // ================= SUPPRIMER PRODUIT =================
    @PostMapping("/delete-product")
    public String deleteProduct(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        logger.info("Request to delete product id={}", id);

        if (id == null || !productRepository.existsById(id)) {
            return "redirect:/admin/dashboard";
        }

        try {
            productRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Produit supprimé.");
        } catch (Exception ex) {
            logger.error("Error deleting product id={}", id, ex);
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer le produit.");
        }

        return "redirect:/admin/dashboard";
    }
}
