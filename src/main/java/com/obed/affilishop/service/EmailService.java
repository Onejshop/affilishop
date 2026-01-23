package com.obed.affilishop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("affilishop43@gmail.com"); // expéditeur (platfòm AffiliShop)
            message.setTo(to); // destinataire (utilisateur ki enskri)
            message.setSubject("Bienvenue sur AffiliShop !");
            message.setText("Bonjour " + username + ",\n\n" +
                    "Merci pour votre inscription sur AffiliShop !\n" +
                    "Votre compte a été créé avec succès.\n\n" +
                    "Vous pouvez maintenant vous connecter en cliquant sur ce lien :\n" +
                    "http://localhost:8080/login\n\n" +
                    "Cordialement,\n" +
                    "L'équipe AffiliShop");

            logger.info("Tentative d'envoi d'email à: {}", to);
            mailSender.send(message);
            logger.info("Email envoyé avec succès à: {}", to);

        } catch (Exception ex) {
            logger.error("Erreur lors de l'envoi d'email à {}: {}", to, ex.getMessage(), ex);
        }
    }
}
