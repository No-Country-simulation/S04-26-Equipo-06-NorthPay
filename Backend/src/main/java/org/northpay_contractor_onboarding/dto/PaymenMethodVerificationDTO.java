package org.northpay_contractor_onboarding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymenMethodVerificationDTO {

    private Boolean isVerified;
    private String verificationNotes;

}
