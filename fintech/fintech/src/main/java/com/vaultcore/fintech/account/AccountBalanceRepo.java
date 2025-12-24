package com.vaultcore.fintech.account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AccountBalanceRepo extends JpaRepository<AccountBalance, UUID> {

@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT b FROM AccountBalance b WHERE b.accountId = :id")
Optional<AccountBalance> lockForUpdate(@Param("id") UUID id);

@Query("SELECT b FROM AccountBalance b WHERE b.accountId = :id")
Optional<AccountBalance> findByAccountId(@Param("id") UUID id);
}
