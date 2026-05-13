package org.northpay_contractor_onboarding.service.implementations;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.InvitationTokenDTO;
import org.northpay_contractor_onboarding.dto.authentication.InvTokenContractorSignUp;
import org.northpay_contractor_onboarding.dto.jwt.JwtClaimsDTO;
import org.northpay_contractor_onboarding.dto.jwt.TokenDTO;
import org.northpay_contractor_onboarding.enums.JwtTypes;
import org.northpay_contractor_onboarding.enums.Roles;
import org.northpay_contractor_onboarding.exception.NotFoundException;
import org.northpay_contractor_onboarding.model.InvitationTokens;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.repository.InvitationTokenRepository;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.northpay_contractor_onboarding.security.authentication.AuthenticatedUserDetails;
import org.northpay_contractor_onboarding.security.jwt.JwtService;
import org.northpay_contractor_onboarding.service.interfaces.IInvitationTokenService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class InvitationTokenService implements IInvitationTokenService {
  private final InvitationTokenRepository invitationTokenRepository;
  private final OnboardingRepository onboardingRepository;
  
  private final BCryptPasswordEncoder encoder;
  private final JwtService jwtService;

  @Override
  public List<InvitationTokenDTO> getAll() {
    return invitationTokenRepository.findAll().stream().map(
      entity -> InvitationTokenDTO.builder()
        .id(entity.getId().toString())
        .tokenUrl(entity.getTokenUrl())
        .used(entity.getUsed())
        .isValid(entity.getIsValid())
        .contractorEmail(entity.getContractorEmail())
        .expiresAt(entity.getExpiresAt().toString())
        .createdAt(entity.getCreatedAt().toString())
        .createdBy(entity.getOperatorEmail().toString())
        .onboardingId(entity.getOnboarding().getId().toString())
      .build()
    ).toList();
  }

  @Override
  public InvitationTokens create(@NotNull UUID onboardingId, @NotNull String contractorEmail, AuthenticatedUserDetails loggedOperator) {
    Onboarding onboardingFather = onboardingRepository.findById(onboardingId).orElseThrow(
      () -> new NotFoundException("Onboarding with id '%s' is not found".formatted(onboardingId.toString()))
    );

    InvitationTokens newInvToken = invitationTokenRepository.save(InvitationTokens.builder()
      .tokenUrl(UUID.randomUUID().toString())
      .used(false)
      .isValid(true)
      .contractorEmail(contractorEmail)
      .operatorEmail(loggedOperator.getEmail())
      .password(null)
      .createdAt(Instant.now())
      .expiresAt(LocalDateTime.now().plusHours(24))
      .onboarding(onboardingFather)
    .build());

    // si el paso anterior es exitoso acá se llamaría al servicio que manda el mail al contratista

    return newInvToken;
  }

  @Override
  public boolean checkInvitationTokenUrlIsExpired(String tokenUrl) {
    return invitationTokenRepository.findByTokenUrl(tokenUrl).orElseThrow(
      () -> new NotFoundException("")
    ).getExpiresAt().isBefore(LocalDateTime.now());
  }
  public boolean checkInvitationTokenUrlIsExpired(InvitationTokens invToken) {
    return invToken.getExpiresAt().isBefore(LocalDateTime.now());
  }

  @Override
  public TokenDTO useTokenForFirstTime(@Valid InvTokenContractorSignUp info) {
    InvitationTokens referredToken = invitationTokenRepository.findByTokenUrl(info.tokenUrl()).orElseThrow(
      () -> new NotFoundException("Invitation with token '%s' not found".formatted(info.tokenUrl()))
    );

    // Validaciones ==========================
    if (!this.checkInvitationTokenUrlIsExpired(referredToken) && !referredToken.getUsed()) {
      invitationTokenRepository.save(referredToken.toBuilder().isValid(false).build());
      // registro de intento
      throw new RuntimeException(""); // TODO: arrojar 403
    }
    if (referredToken.getUsed() && referredToken.getIsValid()) 
      throw new RuntimeException("This invitation token has already been used, cannot set a new password. Use login endpoint instead"); 
      // debe arrojar status 400 que corresponda a que el endpoint no es el correcto
    if (info.password().equals(info.passwordConfirmation()))
      throw new RuntimeException("Passwords aren't the same"); // status 406 0 422?

    // Seteo de password y cambio de estado ================
    invitationTokenRepository.save(referredToken.toBuilder()
      .used(true)
      .password(encoder.encode(info.password()))
    .build());

    return this.login(info.tokenUrl(), info.password());
  }

  @Override
  public TokenDTO login(String tokenUrl, String password) {
    InvitationTokens referredToken = invitationTokenRepository.findByTokenUrl(tokenUrl).orElseThrow(
      () -> new NotFoundException("Invitation with token '%s' not found".formatted(tokenUrl))
    );

    if (!referredToken.getIsValid())
      throw new RuntimeException("This token has been invalidated"); // debe arrojar 403 Forbidden

    if (encoder.matches(password, referredToken.getPassword()))
      throw new RuntimeException("Wrong password"); // status 406

    try {
      AuthenticatedUserDetails newAuthData = new AuthenticatedUserDetails(referredToken.getContractorEmail(), "", "", Roles.CONTRACTOR);
      PreAuthenticatedAuthenticationToken newAuth = new PreAuthenticatedAuthenticationToken(
        newAuthData,
        null
      );
      newAuth.setAuthenticated(true);
      SecurityContextHolder.getContext().setAuthentication(newAuth);

      String token = jwtService.generateToken(new JwtClaimsDTO("", Roles.CONTRACTOR, JwtTypes.contractorAuth), referredToken.getContractorEmail());
      return new TokenDTO(token, "");
    } catch (Exception e) {
      // TODO: handle exception
      throw new RuntimeException("Unknown error in authentication: " + e.getMessage());
    }
  }
}
