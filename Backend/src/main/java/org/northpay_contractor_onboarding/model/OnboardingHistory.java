package org.northpay_contractor_onboarding.model;

import java.time.LocalDateTime;

public class OnboardingHistory {
    private Long id;
    private Onboarding onboarding;
    private String oldStatus;
    private String newStatus;
    private String changedBy;
    private String reason;
    private LocalDateTime changedAt;

    
}
