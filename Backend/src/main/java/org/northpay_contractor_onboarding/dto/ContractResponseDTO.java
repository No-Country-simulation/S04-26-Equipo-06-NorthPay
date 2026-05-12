package org.northpay_contractor_onboarding.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractResponseDTO {

    private String contract_id;
    private String document_url;
    private Boolean signed;
    private String created_at;
    private String onboarding_id;

}
