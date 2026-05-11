package org.northpay_contractor_onboarding.dto.operatorDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OperatorRegistrationDTO {
  @NotBlank
  private String username;
  @NotBlank
  private String email;
  @NotBlank
  private String password;
  @NotBlank
  private String passwordConfirmation;
}
