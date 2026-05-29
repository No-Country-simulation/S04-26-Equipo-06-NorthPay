package org.northpay_contractor_onboarding.dto;

import java.util.UUID;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.model.Onboarding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class IdentityValidationResponseDTO {
    private String status;      
    private double score;        
    private String message;    
    private UUID Onboardingid;  
}
