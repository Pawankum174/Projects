package com.vaultcore.fintech.ledger;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface LedgerRepo extends JpaRepository<LedgerEntry, Long> {

@Query("SELECT COALESCE(SUM(CASE WHEN e.entryType = 'CREDIT' THEN e.amount WHEN e.entryType = 'DEBIT' THEN -e.amount ELSE 0 EN),0) FROM LedgerEntry e WHERE e.txnId = :txnId")
BigDecimal netByTxnId(@Param("txnId") UUID txnId);

@Query("SELECT COALESCE(SUM(CASE WHEN e.entryType = 'CREDIT' THEN e.amount WHEN e.entryType = 'DEBIT'  THEN -e.amount ELSE 0 END), 0) FROM LedgerEntry e WHERE e.accountId = :accountId")
BigDecimal netByAccount(@Param("accountId") UUID accountId);
}
