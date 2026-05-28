package org.northpay_contractor_onboarding.enums;

public enum ApprovalStatus {
    APPROVED,
    CHANGES_REQUESTED,
    PENDING_VERIFICATION;

   public static ApprovalStatus fromOnboardingStatus(OnboardingStatus onboardingStatus) {

        if (onboardingStatus == null) {
            return ApprovalStatus.PENDING_VERIFICATION;
        }
        
        return switch (onboardingStatus) {
            case APPROVED -> ApprovalStatus.APPROVED;
            case CHANGES_REQUESTED -> ApprovalStatus.CHANGES_REQUESTED;
            
            default -> ApprovalStatus.PENDING_VERIFICATION;
        };
    }
}