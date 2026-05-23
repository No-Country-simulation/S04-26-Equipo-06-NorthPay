package org.northpay_contractor_onboarding.dto.onboardingDtos;

import org.northpay_contractor_onboarding.serializers.ProtectDataSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@AllArgsConstructor
@Getter
public class PersonalInfoSection {

   
    private final String fullName;
    private final String email;
    private final String phone;
    private final String country;
    private final String address;

    
  

 

}
