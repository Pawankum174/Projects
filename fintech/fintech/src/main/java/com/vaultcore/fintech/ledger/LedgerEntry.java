package com.vaultcore.fintech.ledger;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID txnId;

    @Column(nullable = false)
    private UUID accountId;

    @Column(nullable = false)
    private String entryType; // DEBIT or CREDIT

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (createdAt == null) createdAt = Instant.now();
    }

    // --- Constructor for convenience ---
    public LedgerEntry(UUID txnId, UUID accountId, String entryType,
                       BigDecimal amount, String currency, String description) {
        this.id = UUID.randomUUID();
        this.txnId = txnId;
        this.accountId = accountId;
        this.entryType = entryType;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.createdAt = Instant.now();
    }

    public LedgerEntry() {}

    // --- Getters ---
    public UUID getId() { return id; }
    public UUID getTxnId() { return txnId; }
    public UUID getAccountId() { return accountId; }
    public String getEntryType() { return entryType; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }
}
