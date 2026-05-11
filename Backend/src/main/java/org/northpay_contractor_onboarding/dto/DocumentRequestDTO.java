package org.northpay_contractor_onboarding.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentRequestDTO {

    private String type;
    private String url;
    private String status;
    private String onboarding_id;

}
