package org.northpay_contractor_onboarding.dto.onboardingDtos;

import java.util.ArrayList;
import java.util.List;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class OnboardingApproveRequest {

    private OnboardingStatus onboardingStatus;
    private String reason ;
   
    
}
