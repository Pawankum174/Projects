package com.vaultcore.fintech.users;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
  @Id
  private UUID id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String passwordHash;

  @Column(nullable = false)
  private String role = "USER";

  @PrePersist
  void pre() { if (id == null) id = UUID.randomUUID(); }

  public UUID getId() { return id; }
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }
}
