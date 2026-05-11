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
    private String iban;
    private String bank_name;
    private String masked_data;
    private LocalDateTime created_at;
    private String onboarding_id;

}
