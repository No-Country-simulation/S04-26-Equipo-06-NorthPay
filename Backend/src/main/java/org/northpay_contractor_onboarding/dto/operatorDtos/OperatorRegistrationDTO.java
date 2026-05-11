package org.northpay_contractor_onboarding.dto.operatorDtos;

import jakarta.validation.constraints.NotBlank;

public record OperatorRegistrationDTO(
  @NotBlank String username,
  @NotBlank String email,
  @NotBlank String password,
  @NotBlank String passwordConfirmation
) {}
