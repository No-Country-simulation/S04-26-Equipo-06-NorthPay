package org.northpay_contractor_onboarding.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignContractRequestDTO {

    private String signature;
}
