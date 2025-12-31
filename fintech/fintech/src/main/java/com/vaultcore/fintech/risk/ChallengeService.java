package com.vaultcore.fintech.risk;
import org.springframework.stereotype.Service;
import com.vaultcore.fintech.service.EmailService;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChallengeService {
    private final OtpCodeRepo otpRepo;
    private final EmailService email;
    private final SecureRandom random = new SecureRandom();

    public ChallengeService(OtpCodeRepo otpRepo, EmailService email ) {
        this.otpRepo = otpRepo;
        this.email = email;
    }

    public UUID issueChallenge(UUID userId) {
        String otp = String.format("%06d", random.nextInt(1_000_000));

        OtpCode entity = new OtpCode();
        entity.setId(UUID.randomUUID());
        entity.setUserId(userId);              // if you kept raw UUID
        entity.setCode(otp);
        entity.setExpiry(Instant.now().plusSeconds(300));
        entity.setCreatedAt(Instant.now());

        otpRepo.save(entity);

        // For demo: treat userId as email address string
        email.sendOtp(userId.toString(), otp);

        return entity.getId();
    }

    public boolean verify(UUID userId, String code) {
        Optional<OtpCode> latest = otpRepo.findTopByUserIdOrderByExpiryDesc(userId);
        if (latest.isEmpty()) return false;

        OtpCode otp = latest.get();
        return otp.getCode().equals(code) && otp.getExpiry().isAfter(Instant.now());
    }
}
