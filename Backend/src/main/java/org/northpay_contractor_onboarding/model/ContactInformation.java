package org.northpay_contractor_onboarding.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor

public class ContactInformation {
    private String email;
    private String country;
    private String phoneNumber;
    private String address;
    public ContactInformation(String email, String country, String phoneNumber, String address) {
        this.email = email;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
    

}
