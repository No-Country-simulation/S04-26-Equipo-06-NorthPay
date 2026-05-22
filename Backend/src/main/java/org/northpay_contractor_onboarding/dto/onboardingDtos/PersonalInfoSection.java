package org.northpay_contractor_onboarding.dto.onboardingDtos;



import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersonalInfoSection {

    private final String fullName;

    // Le clavamos la ruta completa para que no dependa de ningún import raro
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = org.northpay_contractor_onboarding.serializers.ProtectDataSerializer.class)
    private final String email;

    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = org.northpay_contractor_onboarding.serializers.ProtectDataSerializer.class)
    private final String phone;

    private final String country;
    private final String address;

    
    }
