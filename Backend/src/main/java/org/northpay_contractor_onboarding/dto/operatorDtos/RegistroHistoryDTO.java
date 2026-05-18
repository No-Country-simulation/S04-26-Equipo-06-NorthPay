package org.northpay_contractor_onboarding.dto.operatorDtos;

import org.hibernate.boot.internal.Abstract;
import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.model.Onboarding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroHistoryDTO {

    private Onboarding onboarding ;
    private OnboardingStatus oldStatus;
    private OnboardingStatus newStatus;
            private String changedBy ;
            private String reason;
             private String type;
    
}
