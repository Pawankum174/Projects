package com.vaultcore.fintech.risk;
import org.springframework.stereotype.Service;
import com.vaultcore.fintech.auth.EmailService;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;

@Service
public class ChallengeService {
    private final OtpRepo otpRepo;
    private final EmailService email;
    private final SecureRandom random = new SecureRandom();

    public ChallengeService(OtpRepo otpRepo, EmailService email) {
        this.otpRepo = otpRepo;
        this.email = email;
    }

    public String issueChallenge(String userId) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        Otp entity = new Otp(userId, otp, Instant.now().plusSeconds(300)); // explicit type
        otpRepo.save(entity);
        email.sendOtp(userId, otp); // treat userId as email for demo
        return entity.getId().toString();
    }

    public boolean verify(String userId, String code) {
        Optional<Otp> latest = otpRepo.findTopByUserIdOrderByExpiryDesc(userId); // explicit type
        if (latest.isEmpty()) return false;
        Otp otp = latest.get(); // explicit type
        return otp.getCode().equals(code) && otp.getExpiry().isAfter(Instant.now());
    }
}
