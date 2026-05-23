package org.northpay_contractor_onboarding.dto.onboardingDtos;

import java.time.LocalDateTime;

import org.northpay_contractor_onboarding.serializers.ProtectDataSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentAndContractSection {

    @Getter
    private final String platform;
    @Getter
    private final String walletEmail;
    @Getter
    private final String network;
    @Getter
    private final String walletAddress;
    private final String account; 
    @Getter
    private final Boolean contractSigned;

    @JsonSerialize(using = ProtectDataSerializer.class)
    public String getAccount() {
        return this.account;
    }
}
