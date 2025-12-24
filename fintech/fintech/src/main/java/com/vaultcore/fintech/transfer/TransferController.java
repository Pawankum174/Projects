package com.vaultcore.fintech.transfer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
  private final TransferService service;
  public TransferController(TransferService service){ this.service = service; }


//  @PostMapping
//  public ResponseEntity<?> transfer(@RequestBody TransferDto dto) {
//    var txnId = service.transfer(dto.fromAccountId, dto.toAccountId, dto.amount, dto.currency, dto.description);
//    return ResponseEntity.ok(Map.of("txnId", txnId));
//  }
  
  
}
