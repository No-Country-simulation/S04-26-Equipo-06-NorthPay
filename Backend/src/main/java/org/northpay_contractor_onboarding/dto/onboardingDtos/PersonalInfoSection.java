package org.northpay_contractor_onboarding.dto.onboardingDtos;



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
