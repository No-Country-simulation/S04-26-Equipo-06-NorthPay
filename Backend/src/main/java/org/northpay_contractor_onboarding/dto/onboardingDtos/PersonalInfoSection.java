package org.northpay_contractor_onboarding.dto.onboardingDtos;

import org.northpay_contractor_onboarding.serializers.ProtectDataSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@AllArgsConstructor
public class PersonalInfoSection {

    @Getter
    private final String fullName;
    private final String email;
    private final String phone;
    @Getter
    private final String country;
    @Getter
    private final String address;

    @JsonSerialize(using = ProtectDataSerializer.class)
    public String getEmail() {
        return this.email;
    }

    @JsonSerialize(using = ProtectDataSerializer.class)
    public String getPhone() {
        return this.phone;
    }

}
