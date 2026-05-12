package org.northpay_contractor_onboarding.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentMethodRequestDTO {

    private String iban;
    private String bank_name;
    private String masked_data;
    private String onboarding_id;

}
