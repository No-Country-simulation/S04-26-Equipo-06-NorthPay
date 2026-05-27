package org.northpay_contractor_onboarding.dto.onboardingDtos;

import java.util.UUID;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnboardingReviewDTO {
    private UUID id;
    private OnboardingStatus onboardingStatus;
    private String reason;
    private UUID onboardingId;

}
