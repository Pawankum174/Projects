package com.vaultcore.fintech.query;


import org.springframework.web.bind.annotation.*;
import com.vaultcore.fintech.account.AccountBalanceRepo;
import com.vaultcore.fintech.account.AccountBalance;

import org.springframework.http.ResponseEntity;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.concurrent.*;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/accounts")
public class BalanceController {

    private final AccountBalanceRepo balances;

    public BalanceController(AccountBalanceRepo balances) {
        this.balances = balances;
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<?> balance(@PathVariable UUID id) {
        AccountBalance b = balances.findById(id).orElse(null);   // explicit type
        if (b == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(
            java.util.Map.of(
                "accountId", id,
                "balance", b.getBalance(),
                "currency", b.getCurrency()
            )
        );
    }

    @GetMapping("/{id}/balance/stress")
    public ResponseEntity<?> stress(@PathVariable UUID id,
                                    @RequestParam(defaultValue = "5000") int n) throws Exception {
        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) { // explicit type
            List<Callable<BigDecimal>> tasks = IntStream.range(0, n)
                .mapToObj(i -> (Callable<BigDecimal>) () ->
                    balances.findById(id).orElseThrow().getBalance()
                )
                .toList();
            exec.invokeAll(tasks);
        }
        return ResponseEntity.ok("OK");
    }
}
