package com.vaultcore.fintech.transfer;
import java.math.BigDecimal;
import java.util.UUID;


public interface TransferService {

    UUID transfer(UUID fromAccountId,
                  UUID toAccountId,
                  BigDecimal amount,
                  String currency,
                  String description);

    TransferResponseDto getTransfer(UUID txnId);
}

//public class TransferService {
//private final LedgerRepo ledger;
//private final AccountBalanceRepo balances;
//
//public TransferService(LedgerRepo ledger, AccountBalanceRepo balances) {
// this.ledger = ledger; this.balances = balances;
//}
//
//@Transactional(isolation = Isolation.SERIALIZABLE)
//public UUID transfer(UUID from, UUID to, BigDecimal amount, String currency, String desc) {
//    UUID txnId = UUID.randomUUID();
//    ledger.save(new LedgerEntry(txnId, from, "DEBIT", amount, currency, desc));
//    ledger.save(new LedgerEntry(txnId, to, "CREDIT", amount, currency, desc));
//
//    AccountBalance fb = balances.lockForUpdate(from).orElseThrow();
//    AccountBalance tb = balances.lockForUpdate(to).orElseThrow();
//
//    fb.setBalance(fb.getBalance().subtract(amount));
//    tb.setBalance(tb.getBalance().add(amount));
//
//    balances.save(fb);
//    balances.save(tb);
//
//    return txnId;
//}
//
//}
