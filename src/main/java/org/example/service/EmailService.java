package org.example.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendResetPasswordEmail(String email, String resetUrl) {
        // Логика отправки email, например, через JavaMailSender
        System.out.println("Sending reset password email to " + email);
        System.out.println("Reset URL: " + resetUrl);
    }
}
