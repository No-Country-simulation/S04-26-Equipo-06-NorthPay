package org.northpay_contractor_onboarding.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginDTO {
  @NotBlank
  private String email;
  @NotBlank
  private String password;
}
