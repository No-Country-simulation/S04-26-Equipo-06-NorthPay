package org.northpay_contractor_onboarding.dto.onboardingDtos;

import java.time.LocalDateTime;
import java.util.UUID;

import org.northpay_contractor_onboarding.enums.ApprovalStatus;
import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.model.Onboarding;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class OnboardingSummaryDTO {
    private UUID id; 
    private String applicantName; 
    private String applicantEmail; 
    private OnboardingStatus status; 
    private LocalDateTime updatedAt;
    private ApprovalStatus approvalStatus;


    private LocalDateTime createdAt;
    private Integer currentStep;

    public OnboardingSummaryDTO(ApprovalStatus approvalStatus , Onboarding onboarding) {
    this.id = onboarding.getId();
    this.applicantEmail = onboarding.getContractor().getEmail();
    this.applicantName = onboarding.getContractor().getFirstName() + " " + onboarding.getContractor().getLastName();
    this.status = onboarding.getStatus();
    this.updatedAt = onboarding.getUpdatedAt();
    this.createdAt = onboarding.getCreatedAt();
    this.currentStep = onboarding.getCurrentStep();
    this.approvalStatus = approvalStatus;
     
    }

    
}
