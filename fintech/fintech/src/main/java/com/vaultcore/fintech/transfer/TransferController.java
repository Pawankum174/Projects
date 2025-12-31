package com.vaultcore.fintech.transfer;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> transfer(@Valid @RequestBody TransferRequestDto dto) {
        UUID txnId = service.transfer(
                dto.getFromAccountId(),
                dto.getToAccountId(),
                dto.getAmount(),
                dto.getCurrency(),
                dto.getDescription()
        );
        return ResponseEntity.ok(Map.of("txnId", txnId));
    }

    @GetMapping("/{txnId}")
    public ResponseEntity<TransferResponseDto> getTransfer(@PathVariable UUID txnId) {
        TransferResponseDto response = service.getTransfer(txnId);
        return ResponseEntity.ok(response);
    }
}
