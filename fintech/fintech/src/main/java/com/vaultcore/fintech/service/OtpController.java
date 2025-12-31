package com.vaultcore.fintech.service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vaultcore.fintech.risk.OtpCode;
import com.vaultcore.fintech.risk.OtpCodeDto;
import com.vaultcore.fintech.risk.OtpCodeRepo;
import com.vaultcore.fintech.risk.OtpMapper;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/auth/otp")
public class OtpController {

    private final OtpCodeRepo otpCodeRepo;

    public OtpController(OtpCodeRepo otpCodeRepo) {
        this.otpCodeRepo = otpCodeRepo;
    }

    @PostMapping("/generate")
    public ResponseEntity<OtpCodeDto> generateOtp(@RequestBody OtpGenerateRequestDto dto) {
        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1_000_000));
        Instant expiry = Instant.now().plusSeconds(300);

        OtpCode otp = new OtpCode();
        otp.setId(UUID.randomUUID());
        otp.setUserId(dto.getUserId());
        otp.setCode(code);
        otp.setExpiry(expiry);
        otp.setCreatedAt(Instant.now());

        otpCodeRepo.save(otp);

        // Return DTO instead of raw entity
        return ResponseEntity.ok(OtpMapper.toDto(otp));
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody OtpVerifyRequestDto dto) {
        var otp = otpCodeRepo.findByUserIdAndCode(dto.getUserId(), dto.getCode())
                .filter(o -> o.getExpiry().isAfter(Instant.now()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired OTP"));

        otpCodeRepo.delete(otp);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "OTP verified successfully",
                "userId", dto.getUserId()
        ));
    }

}
