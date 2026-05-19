package org.northpay_contractor_onboarding.dto;


import lombok.*;
import org.northpay_contractor_onboarding.enums.PaymentMethodTypes;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentMethodRequestDTO {

    private PaymentMethodTypes paymentMethodType;
    private String platform;
    private String walletEmail;
    private String network;
    private String walletAddress;
    private Boolean isPaymentVerified;
    private String verificationNotes;
    private UUID onboarding_id;

}
