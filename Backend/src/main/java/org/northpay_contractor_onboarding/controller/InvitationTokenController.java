package org.northpay_contractor_onboarding.controller;

import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.InvitationTokenDTO;
import org.northpay_contractor_onboarding.dto.authentication.ContractorLoginDTO;
import org.northpay_contractor_onboarding.dto.authentication.InvTokenContractorSignUp;
import org.northpay_contractor_onboarding.dto.jwt.TokenDTO;
import org.northpay_contractor_onboarding.security.authentication.AuthenticatedUserDetails;
import org.northpay_contractor_onboarding.service.implementations.InvitationTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invitation-token")
@Validated
public class InvitationTokenController {
  private final InvitationTokenService invTokenService;

  @GetMapping("")
  @PreAuthorize("hasAnyRole('ROLE_OPERATOR')")
  public ResponseEntity<List<InvitationTokenDTO>> getAll() {
    return new ResponseEntity<>(invTokenService.getAll(), HttpStatus.OK);
  }
  
  @PostMapping("")
  @PreAuthorize("hasAnyRole('ROLE_OPERATOR')")
  public ResponseEntity<InvitationTokenDTO> createToken(
    @NotBlank @RequestParam String onboardingId,
    @NotBlank @RequestParam String contractorEmail,
    @AuthenticationPrincipal AuthenticatedUserDetails authOperator
  ) {
    return new ResponseEntity<>(
      invTokenService.create(UUID.fromString(onboardingId), contractorEmail, authOperator),
      HttpStatus.CREATED
    );
  }

  @PatchMapping("/first-time-use")
  public ResponseEntity<TokenDTO> useTokenForFirstTime(@Valid @RequestBody InvTokenContractorSignUp data) {
    return new ResponseEntity<>(invTokenService.useTokenForFirstTime(data), HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<TokenDTO> postMethodName(@Valid @RequestBody ContractorLoginDTO info) {
    return new ResponseEntity<>(invTokenService.login(info), HttpStatus.OK);
  }
  
}
