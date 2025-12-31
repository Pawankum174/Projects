package com.vaultcore.fintech.risk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpCodeRepo extends JpaRepository<OtpCode, UUID> {

	
	Optional<OtpCode> findTopByUserIdOrderByExpiryDesc(UUID userId);

    // Find OTP by user and code
    Optional<OtpCode> findByUserIdAndCode(UUID userId, String code);

    // Find all OTPs for a user
    java.util.List<OtpCode> findByUserId(UUID userId);

    // Optionally, delete expired codes
    void deleteByExpiryBefore(java.time.Instant now);
}
