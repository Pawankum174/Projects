package com.vaultcore.fintech.service;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class OtpVerifyRequestDto {

    @NotNull
    private UUID userId;

    @NotNull
    private String code;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
