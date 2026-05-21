package org.northpay_contractor_onboarding.dto.onboardingDtos;

import java.util.UUID;

import org.northpay_contractor_onboarding.model.ContactInformation;
import org.northpay_contractor_onboarding.model.Onboarding;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DataPersonalDTO {


    private UUID id;
    private String firstName;
    private String lastName;
    private String textId;
    private ContactInformation contactInformation;
    public DataPersonalDTO(Onboarding onboarding) {
        this.id = onboarding.getContractor().getId();
        this.firstName = onboarding.getContractor().getFirstName();
         this.lastName = onboarding.getContractor().getLastName();
        this.textId = onboarding.getContractor().getTextId();
        this.contactInformation = onboarding.getContractor().getContactInformation();
    }

    
    
}
