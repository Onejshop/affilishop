package com.obed.affilishop.controller;

import com.obed.affilishop.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestEmailController {

    private final EmailService emailService;

    public TestEmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send-test")
public String sendTest(@RequestParam String to, @RequestParam String username) throws IOException {
    emailService.sendWelcomeEmail(to, username);
    return "Email de bienvenue envoyé à: " + to + " ✅";
}

}
