package org.northpay_contractor_onboarding.enums;

public enum OnboardingStatus {
    INVITED,
    PERSONAL_DATA_COMPLETED,
    DOCUMENTS_UPLOADED,
    CONTRACT_SIGNED,
    PAYMENT_CONFIGURED,
    PENDING_VERIFICATION,
    CHANGES_REQUESTED,
    IDENTITY_VERIFICATION_COMPLETED,
    APPROVED;

    public boolean canTransitionTo(OnboardingStatus nextState) {
        return switch (this) {
            case INVITED -> nextState == PERSONAL_DATA_COMPLETED;
            case PERSONAL_DATA_COMPLETED -> nextState == DOCUMENTS_UPLOADED;
            case DOCUMENTS_UPLOADED -> nextState == CONTRACT_SIGNED;
            case CONTRACT_SIGNED -> nextState == PAYMENT_CONFIGURED;
            case PAYMENT_CONFIGURED -> nextState == IDENTITY_VERIFICATION_COMPLETED;
            case IDENTITY_VERIFICATION_COMPLETED -> nextState == PENDING_VERIFICATION;
            

            case PENDING_VERIFICATION -> nextState == APPROVED || nextState == CHANGES_REQUESTED;

            case CHANGES_REQUESTED -> nextState == PENDING_VERIFICATION;

            case APPROVED -> false;
            default -> false;
        };
    }

}
