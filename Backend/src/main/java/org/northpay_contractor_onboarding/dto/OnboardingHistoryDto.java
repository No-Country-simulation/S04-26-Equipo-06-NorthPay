package org.northpay_contractor_onboarding.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnboardingHistoryDto {

    private UUID onboarding;
    private OnboardingStatus oldStatus;
    private OnboardingStatus newStatus;
    private String changedBy;
    private String reason;
    private LocalDateTime changedAt;

}
