package com.vaultcore.fintech.account;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "account_balances")
public class AccountBalance {
  @Id
  private UUID accountId;

  @Column(nullable = false)
  private BigDecimal balance = BigDecimal.ZERO;

  @Column(nullable = false, length = 3)
  private String currency;

  public UUID getAccountId() { return accountId; }
  public void setAccountId(UUID accountId) { this.accountId = accountId; }
  public BigDecimal getBalance() { return balance; }
  public void setBalance(BigDecimal balance) { this.balance = balance; }
  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }
}
