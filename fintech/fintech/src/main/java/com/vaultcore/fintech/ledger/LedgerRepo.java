package com.vaultcore.fintech.ledger;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LedgerRepo extends JpaRepository<LedgerEntry, UUID> {
	LedgerEntry findByTxnIdAndEntryType(UUID txnId, String entryType);
	}