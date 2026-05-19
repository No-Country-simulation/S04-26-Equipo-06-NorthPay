package org.northpay_contractor_onboarding.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractRequestDTO {

    private String content;
    private String signedBy;
    private LocalDateTime signedAt;
    private String contractHash;
    private String signatureReference;
    private String status;
    private Boolean signed;
    private LocalDateTime created_at;
    private String onboardingId;

}
