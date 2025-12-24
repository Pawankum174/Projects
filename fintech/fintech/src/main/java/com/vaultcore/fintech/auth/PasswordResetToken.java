package com.vaultcore.fintech.auth;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
@Id
@GeneratedValue
private UUID id;

@Column(nullable = false, unique = true)
private String token;

@Column(nullable = false)
private UUID userId;

@Column(nullable = false)
private Instant expiry;

public PasswordResetToken() {}
public PasswordResetToken(String token, UUID userId, Instant expiry) {
 this.token = token; this.userId = userId; this.expiry = expiry;
}

public UUID getId() { return id; }
public String getToken() { return token; }
public void setToken(String token) { this.token = token; }
public UUID getUserId() { return userId; }
public void setUserId(UUID userId) { this.userId = userId; }
public Instant getExpiry() { return expiry; }
public void setExpiry(Instant expiry) { this.expiry = expiry; }
}
