package com.vaultcore.fintech.risk;



import java.time.Instant;
import java.util.UUID;

public record OtpCodeDto(
        UUID userId,
        String code,
        Instant expiry
) {}
