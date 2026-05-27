package org.northpay_contractor_onboarding.dto.onboardingDtos;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class OnboardingReviewRequest {
      private OnboardingStatus onboardingStatus;
    private String reason;

}
