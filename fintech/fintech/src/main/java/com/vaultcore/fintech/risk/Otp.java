package com.vaultcore.fintech.risk;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "otp_codes")
public class Otp {
@Id
private UUID id;
private String userId;
private String code;
private Instant expiry;

public Otp() {}

public Otp(String userId, String code, Instant expiry) {
 this.id = UUID.randomUUID();
 this.userId = userId;
 this.code = code;
 this.expiry = expiry;
}

public UUID getId() { return id; }
public String getUserId() { return userId; }
public String getCode() { return code; }
public Instant getExpiry() { return expiry; }
}

