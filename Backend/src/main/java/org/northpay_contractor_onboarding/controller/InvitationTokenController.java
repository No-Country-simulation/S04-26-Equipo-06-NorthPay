package org.northpay_contractor_onboarding.controller;

import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.InvitationTokenDTO;
import org.northpay_contractor_onboarding.dto.authentication.ContractorLoginDTO;
import org.northpay_contractor_onboarding.dto.authentication.InvTokenContractorSignUp;
import org.northpay_contractor_onboarding.dto.jwt.TokenDTO;
import org.northpay_contractor_onboarding.security.authentication.AuthenticatedUserDetails;
import org.northpay_contractor_onboarding.service.interfaces.IInvitationTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invitation-token")
@Validated
public class InvitationTokenController {
  private final IInvitationTokenService invTokenService;

  @GetMapping("")
  @PreAuthorize("hasAnyRole('OPERATOR')")
  @Operation(
    summary = "Get all invitation tokens",
    security = @SecurityRequirement(name = "bearerAuth")
  )
  public ResponseEntity<List<InvitationTokenDTO>> getAll() {
    return new ResponseEntity<>(invTokenService.getAll(), HttpStatus.OK);
  }

  @GetMapping("/validate/{tokenUrl}")
  public ResponseEntity<InvitationTokenDTO> validateToken(@PathVariable String tokenUrl) {
    return new ResponseEntity<>(invTokenService.validateAndGetTokenData(tokenUrl), HttpStatus.OK);
  }
  
  @PostMapping("")
  @PreAuthorize("hasAnyRole('OPERATOR')")
  @Operation(
    summary = "Create an invitation token manually",
    security = @SecurityRequirement(name = "bearerAuth")
  )
  @ResponseStatus(code = HttpStatus.CREATED)
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
  // OpenAPI annotation
  @Operation(
    description = "When the contractor uses token for first time and create a password. Use `/invitation-token/login` after"
  )
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> useTokenForFirstTime(@Valid @RequestBody InvTokenContractorSignUp data) {
    invTokenService.useTokenForFirstTime(data);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/login")
  @Operation(summary = "Contractor login")
  public ResponseEntity<TokenDTO> login(@Valid @RequestBody ContractorLoginDTO info) {
    return new ResponseEntity<>(invTokenService.login(info), HttpStatus.OK);
  }
  
  @PatchMapping("/invalidate")
  @PreAuthorize("hasAnyRole('OPERATOR')")
  @SecurityRequirement(name = "bearerAuth")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> invalidate(@NotBlank String tokenUrl) {
    invTokenService.invalidateToken(tokenUrl);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
