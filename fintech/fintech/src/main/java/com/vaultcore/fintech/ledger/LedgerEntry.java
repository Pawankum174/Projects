package com.vaultcore.fintech.ledger;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private UUID txnId;

  @Column(nullable = false)
  private UUID accountId;

  @Column(nullable = false)
  private String entryType; // DEBIT or CREDIT

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false, length = 3)
  private String currency;

  private String description;

  @Column(nullable = false)
  private Instant createdAt = Instant.now();

  public LedgerEntry() {}
  public LedgerEntry(UUID txnId, UUID accountId, String entryType, BigDecimal amount, String currency, String description) {
    this.txnId = txnId; this.accountId = accountId; this.entryType = entryType;
    this.amount = amount; this.currency = currency; this.description = description;
  }

  public Long getId() { return id; }
  public UUID getTxnId() { return txnId; }
  public UUID getAccountId() { return accountId; }
  public String getEntryType() { return entryType; }
  public BigDecimal getAmount() { return amount; }
  public String getCurrency() { return currency; }
  public String getDescription() { return description; }
  public Instant getCreatedAt() { return createdAt; }
}
