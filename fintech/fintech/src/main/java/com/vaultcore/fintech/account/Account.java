package com.vaultcore.fintech.account;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {
  @Id
  private UUID id;

  @Column(nullable = false)
  private UUID userId;

  @Column(nullable = false, length = 3)
  private String currency;

  @Column(nullable = false)
  private String status = "ACTIVE";

  @PrePersist
  void pre() { if (id == null) id = UUID.randomUUID(); }

  public UUID getId() { return id; }
  public UUID getUserId() { return userId; }
  public void setUserId(UUID userId) { this.userId = userId; }
  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
}
