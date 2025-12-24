package com.vaultcore.fintech.auth;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
private final JavaMailSender mailSender;

public EmailService(JavaMailSender mailSender) {
 this.mailSender = mailSender;
}

public void sendPasswordResetEmail(String to, String resetLink) {
 SimpleMailMessage msg = new SimpleMailMessage();
 msg.setTo(to);
 msg.setSubject("VaultCore Password Reset");
 msg.setText("Click the link to reset your password:\n\n" + resetLink);
 mailSender.send(msg);
}

public void sendOtp(String to, String otp) {
 SimpleMailMessage msg = new SimpleMailMessage();
 msg.setTo(to);
 msg.setSubject("VaultCore OTP");
 msg.setText("Your one-time code is: " + otp + " (valid for 5 minutes)");
 mailSender.send(msg);
}
}
