package com.obed.affilishop;

import com.obed.affilishop.model.Product;
import com.obed.affilishop.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AffilishopApplication {

    public static void main(String[] args) {
        SpringApplication.run(AffilishopApplication.class, args);
        System.out.println("******************************************************");  
        System.out.println("Affilishop Application Started Successfully!");  
        System.out.println("******************************************************");  
    }

    // Initialisation des produits au démarrage
    @Bean
    CommandLineRunner initData(ProductRepository repo) {
        return args -> {
            repo.save(new Product(null, "Casque Bluetooth", "Casque sans fil haute qualité", 49.99,
                    "https://via.placeholder.com/300", "https://example.com/affil1", "Amazon"));
            repo.save(new Product(null, "Montre Connectée", "Montre intelligente sport", 79.99,
                    "https://via.placeholder.com/300", "https://example.com/affil2", "AliExpress"));
            repo.save(new Product(null, "Écouteurs Sans Fil", "Écouteurs haute fidélité", 29.99,
                    "https://via.placeholder.com/300", "https://example.com/affil3", "eBay"));
        };
    }
}
