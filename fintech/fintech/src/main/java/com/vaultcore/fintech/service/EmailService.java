package com.vaultcore.fintech.service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a password reset email with a reset link.
     */
    public void sendPasswordResetEmail(String to, String link) {
        System.out.printf("Mock password reset email: to=%s, link=%s%n", to, link);

        // Uncomment for real SMTP
        /*
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("VaultCore Password Reset");
        message.setText("Click the following link to reset your password:\n" + link);
        mailSender.send(message);
        */
    }

    /**
     * Sends a one-time password (OTP) code.
     */
    public void sendOtp(String to, String otp) {
        System.out.printf("Mock OTP email: to=%s, otp=%s%n", to, otp);

        // Uncomment for real SMTP
        /*
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("VaultCore OTP Code");
        message.setText("Your one-time password is: " + otp + "\nIt expires in 5 minutes.");
        mailSender.send(message);
        */
    }
}
