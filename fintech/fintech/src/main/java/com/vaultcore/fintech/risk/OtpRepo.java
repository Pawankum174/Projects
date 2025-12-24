package com.vaultcore.fintech.risk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OtpRepo extends JpaRepository<Otp, UUID> {
Optional<Otp> findTopByUserIdOrderByExpiryDesc(String userId);
}

