package com.vaultcore.fintech.transfer;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class TransferResponseDto {

    private UUID txnId;
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
    private String currency;
    private String description;
    private Instant createdAt;

    public TransferResponseDto(UUID txnId, UUID fromAccountId, UUID toAccountId,
                               BigDecimal amount, String currency, String description, Instant createdAt) {
        this.txnId = txnId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters
    public UUID getTxnId() { return txnId; }
    public UUID getFromAccountId() { return fromAccountId; }
    public UUID getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }
}
