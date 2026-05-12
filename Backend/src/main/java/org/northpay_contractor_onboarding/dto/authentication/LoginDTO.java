package org.northpay_contractor_onboarding.dto.authentication;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
  @NotBlank String email,
  @NotBlank String password
) {}
