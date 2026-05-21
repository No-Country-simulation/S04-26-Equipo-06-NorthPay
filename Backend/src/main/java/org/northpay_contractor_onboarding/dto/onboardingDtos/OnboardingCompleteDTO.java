package org.northpay_contractor_onboarding.dto.onboardingDtos;

import java.lang.annotation.Documented;
import java.util.UUID;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.enums.PaymentMethodTypes;
import org.northpay_contractor_onboarding.model.Contract;
import org.northpay_contractor_onboarding.model.Onboarding;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class OnboardingCompleteDTO {

     private UUID id;
    private Integer currentStep;
    private OnboardingStatus status;
    private String completeName;
    private String email;
    private PaymentMethodTypes paymentMethodTypes;
    //TODO ahora lo completo con todos los detalles de los pasos
    public OnboardingCompleteDTO(Onboarding onboarding) {
        this.currentStep = onboarding.getCurrentStep();
        this.status = onboarding.getStatus();
        this.completeName = onboarding.getContractor().getFirstName() + onboarding.getContractor().getLastName();
        this.email = onboarding.getContractor().getEmail();
        this.paymentMethodTypes = onboarding.getPaymentMethod().getPaymentMethodType();
    }
   

   




   
    
}
