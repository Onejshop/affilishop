package com.obed.affilishop.config;

import com.obed.affilishop.model.User;
import com.obed.affilishop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initializeData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Vérifier si le compte Obed existe
            if (!userRepository.existsByUsername("Obed")) {
                logger.info("Création du compte admin 'Obed'...");
                User adminUser = new User();
                adminUser.setUsername("Obed");
                adminUser.setEmail("obedpamy@gmail.");
                adminUser.setPassword(passwordEncoder.encode("@&Obed$$481473")); // mot de passe encodé
                adminUser.setRole("ADMIN");
                
                userRepository.save(adminUser);
                logger.info("✅ Compte admin 'Obed' créé avec succès !");
            } else {
                logger.info("ℹ️ Le compte 'Obed' existe déjà. Mise à jour du mot de passe et du rôle...");
                
                // Mettre à jour le compte Obed avec le rôle ADMIN et mot de passe encodé
                User existingUser = userRepository.findByUsername("Obed").orElse(null);
                if (existingUser != null) {
                    // Toujours mettre à jour le mot de passe avec le nouveau
                    existingUser.setPassword(passwordEncoder.encode("@&Obed$$481473"));
                    
                    // Vérifier et mettre à jour le rôle
                    if (!existingUser.getRole().equals("ADMIN")) {
                        existingUser.setRole("ADMIN");
                    }
                    
                    userRepository.save(existingUser);
                    logger.info("✅ Compte 'Obed' mis à jour avec le nouveau mot de passe et rôle ADMIN !");
                }
            }
        };
    }
}
