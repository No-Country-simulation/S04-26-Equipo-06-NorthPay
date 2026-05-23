package org.northpay_contractor_onboarding.dto.onboardingDtos;

import java.time.LocalDateTime;

import org.northpay_contractor_onboarding.serializers.ProtectDataSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentAndContractSection {

    private final String platform;
    private final String walletEmail;
    private final String network;
    private final String walletAddress;
    private final String account;
    private final Boolean contractSigned;

}
