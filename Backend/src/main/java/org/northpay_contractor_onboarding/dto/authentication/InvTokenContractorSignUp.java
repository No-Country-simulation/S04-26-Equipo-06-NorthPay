package org.northpay_contractor_onboarding.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record InvTokenContractorSignUp(
  @NotBlank String tokenUrl,
  @NotBlank String password,
  @NotBlank String passwordConfirmation
) {}