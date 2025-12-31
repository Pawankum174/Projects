package com.vaultcore.fintech.risk;
public class OtpMapper {

    public static OtpCodeDto toDto(OtpCode otp) {
        return new OtpCodeDto(
                otp.getUserId(),
                otp.getCode(),
                otp.getExpiry()
        );
    }
}
