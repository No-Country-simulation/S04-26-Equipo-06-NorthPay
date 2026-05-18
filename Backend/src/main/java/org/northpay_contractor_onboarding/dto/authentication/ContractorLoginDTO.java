package org.northpay_contractor_onboarding.dto.authentication;

import org.hibernate.validator.constraints.UUID;

import jakarta.validation.constraints.NotBlank;

public record ContractorLoginDTO(
  @UUID String tokenUrl,
  @NotBlank String password
) {}
