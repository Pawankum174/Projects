package com.vaultcore.fintech.account;

import jakarta.persistence.*;
import java.util.UUID;

import com.vaultcore.fintech.users.User;


@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)  // foreign key column
    private User user;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private String status = "ACTIVE";

    @PrePersist
    void pre() { if (id == null) id = UUID.randomUUID(); }

    // Getters and setters
    public UUID getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
