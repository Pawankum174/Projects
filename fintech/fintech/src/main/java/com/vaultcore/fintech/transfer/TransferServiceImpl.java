package com.vaultcore.fintech.transfer;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaultcore.fintech.account.AccountRepo;
import com.vaultcore.fintech.ledger.LedgerEntry;
import com.vaultcore.fintech.ledger.LedgerRepo;

@Service
public class TransferServiceImpl implements TransferService {

    private final LedgerRepo ledgerRepository;
    private final AccountRepo accountRepository;

    public TransferServiceImpl(LedgerRepo ledgerRepository,
                               AccountRepo accountRepository) {
        this.ledgerRepository = ledgerRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public UUID transfer(UUID fromAccountId,
                         UUID toAccountId,
                         BigDecimal amount,
                         String currency,
                         String description) {

        UUID txnId = UUID.randomUUID();

        // Lock both accounts before modifying
        accountRepository.lockAccount(fromAccountId, currency)
                .orElseThrow(() -> new IllegalArgumentException("From account not found"));
        accountRepository.lockAccount(toAccountId, currency)
                .orElseThrow(() -> new IllegalArgumentException("To account not found"));

        // Perform updates
        accountRepository.debit(fromAccountId, amount, currency);
        accountRepository.credit(toAccountId, amount, currency);

        // Record ledger entries
        ledgerRepository.save(new LedgerEntry(txnId, fromAccountId, "DEBIT", amount, currency, description));//the constructor LedgerEntry(UUID, UUID, String, BigDecimal, String, String) is undefined
        ledgerRepository.save(new LedgerEntry(txnId, toAccountId, "CREDIT", amount, currency, description));//

        return txnId;
    }


    @Override
    public TransferResponseDto getTransfer(UUID txnId) {
        LedgerEntry debit = ledgerRepository.findByTxnIdAndEntryType(txnId, "DEBIT");
        LedgerEntry credit = ledgerRepository.findByTxnIdAndEntryType(txnId, "CREDIT");

        return new TransferResponseDto(
                txnId,
                debit.getAccountId(),
                credit.getAccountId(),
                debit.getAmount(),
                debit.getCurrency(),
                debit.getDescription(),
                debit.getCreatedAt() != null ? debit.getCreatedAt() : Instant.now()
        );
    }
}
