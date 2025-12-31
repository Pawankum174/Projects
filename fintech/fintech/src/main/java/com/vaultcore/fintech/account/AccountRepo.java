package com.vaultcore.fintech.account;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepo extends JpaRepository<AccountBalance, UUID> {

    // Lock the account row before updating
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AccountBalance a WHERE a.accountId = :accountId AND a.currency = :currency")
    Optional<AccountBalance> lockAccount(UUID accountId, String currency);

    @Modifying
    @Transactional
    @Query("UPDATE AccountBalance a SET a.balance = a.balance - :amount WHERE a.accountId = :accountId AND a.currency = :currency")
    void debit(UUID accountId, BigDecimal amount, String currency);

    @Modifying
    @Transactional
    @Query("UPDATE AccountBalance a SET a.balance = a.balance + :amount WHERE a.accountId = :accountId AND a.currency = :currency")
    void credit(UUID accountId, BigDecimal amount, String currency);
}
