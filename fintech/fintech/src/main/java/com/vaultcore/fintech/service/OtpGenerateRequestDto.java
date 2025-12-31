package com.vaultcore.fintech.service;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class OtpGenerateRequestDto {

    @NotNull
    private UUID userId;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
}
