package org.northpay_contractor_onboarding.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentMethodResponseDTO {

    private String payment_method_id;
    private String payment_method_type;
    private String platform;
    private String wallet_email;
    private String network;
    private String wallet_address;
    private Boolean is_payment_verified;
    private String verification_notes;
    private LocalDateTime created_at;
    private String onboarding_id;

}
