package org.northpay_contractor_onboarding.service.implementations;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.ContractorNameDTO;
import org.northpay_contractor_onboarding.dto.InvitationTokenDTO;
import org.northpay_contractor_onboarding.dto.authentication.ContractorLoginDTO;
import org.northpay_contractor_onboarding.dto.authentication.InvTokenContractorSignUp;
import org.northpay_contractor_onboarding.dto.jwt.JwtClaimsDTO;
import org.northpay_contractor_onboarding.dto.jwt.TokenDTO;
import org.northpay_contractor_onboarding.enums.JwtTypes;
import org.northpay_contractor_onboarding.enums.Roles;
import org.northpay_contractor_onboarding.exception.AlreadyExistsException;
import org.northpay_contractor_onboarding.exception.InvalidTokenException;
import org.northpay_contractor_onboarding.exception.NotFoundException;
import org.northpay_contractor_onboarding.model.InvitationTokens;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.repository.InvitationTokenRepository;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.northpay_contractor_onboarding.security.authentication.AuthenticatedUserDetails;
import org.northpay_contractor_onboarding.security.jwt.JwtService;
import org.northpay_contractor_onboarding.service.interfaces.IInvitationTokenService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import io.jsonwebtoken.ExpiredJwtException;
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
            .build())
        .toList();
  }

  @Override
  public InvitationTokenDTO create(UUID onboardingId, String contractorEmail, AuthenticatedUserDetails loggedOperator) {
    Onboarding onboardingFather = onboardingRepository.findById(onboardingId).orElseThrow(
        () -> new NotFoundException("Onboarding with id '%s' is not found".formatted(onboardingId.toString())));

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

    return InvitationTokenDTO.builder()
        .id(newInvToken.getId().toString())
        .tokenUrl(newInvToken.getTokenUrl())
        .used(newInvToken.getUsed())
        .isValid(newInvToken.getIsValid())
        .contractorEmail(newInvToken.getContractorEmail())
        .expiresAt(newInvToken.getExpiresAt().toString())
        .createdAt(newInvToken.getCreatedAt().toString())
        .createdBy(newInvToken.getOperatorEmail().toString())
        .onboardingId(newInvToken.getOnboarding().getId().toString())
        .build();
  }

  @Override
  public boolean checkInvitationTokenUrlIsExpired(String tokenUrl) {
    return invitationTokenRepository.findByTokenUrl(tokenUrl).orElseThrow(
        () -> new NotFoundException("Invitation with token '%s' not found".formatted(tokenUrl))).getExpiresAt()
        .isBefore(LocalDateTime.now());
  }

  public boolean checkInvitationTokenUrlIsExpired(InvitationTokens invToken) {
    return invToken.getExpiresAt().isBefore(LocalDateTime.now());
  }

  @Override
  public InvitationTokenDTO validateAndGetTokenData(String tokenUrl) {
    InvitationTokens referredToken = invitationTokenRepository.findByTokenUrl(tokenUrl).orElseThrow(
      () -> new NotFoundException("Invitation with token '%s' not found".formatted(tokenUrl))
    );

    if (referredToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      if (referredToken.getIsValid()) {
          invitationTokenRepository.save(referredToken.toBuilder().isValid(false).build());
      }
      throw new ExpiredJwtException(null, null, "Expired token");
    }

    if (referredToken.getUsed() || !referredToken.getIsValid()) {
      throw new ExpiredJwtException(null, null, "Expired or already used token");
    }

    return InvitationTokenDTO.builder()
      .id(referredToken.getId().toString())
      .tokenUrl(referredToken.getTokenUrl())
      .used(referredToken.getUsed())
      .isValid(referredToken.getIsValid())
      .contractorEmail(referredToken.getContractorEmail())
      .expiresAt(referredToken.getExpiresAt().toString())
      .createdAt(referredToken.getCreatedAt().toString())
      .createdBy(referredToken.getOperatorEmail().toString())
      .onboardingId(referredToken.getOnboarding().getId().toString())
      .build();
  }

  @Override
  public void useTokenForFirstTime(InvTokenContractorSignUp info) {
    InvitationTokens referredToken = invitationTokenRepository.findByTokenUrl(info.tokenUrl()).orElseThrow(
        () -> new NotFoundException("Invitation with token '%s' not found".formatted(info.tokenUrl())));

    if (this.checkInvitationTokenUrlIsExpired(referredToken) && !referredToken.getUsed()) {
      invitationTokenRepository.save(referredToken.toBuilder().isValid(false).build()); 
      throw new ExpiredJwtException(null, null, "Expired invitation token.");
    }
    if (referredToken.getUsed() && referredToken.getIsValid())
      throw new AlreadyExistsException(
          "This invitation token has already been used, cannot set a new password. Use login endpoint instead");

    if (!info.password().equals(info.passwordConfirmation()))
      throw new BadCredentialsException("Passwords aren't the same");

    invitationTokenRepository.save(referredToken.toBuilder()
        .used(true)
        .password(encoder.encode(info.password()))
        .activatedAt(LocalDateTime.now())
        .build());
  }

  @Override
  public TokenDTO login(ContractorLoginDTO loginInfo) {
    InvitationTokens referredToken = invitationTokenRepository.findByTokenUrl(loginInfo.tokenUrl()).orElseThrow(
        () -> new NotFoundException("Invitation with token '%s' not found".formatted(loginInfo.tokenUrl())));

    if (!referredToken.getIsValid())
      throw new InvalidTokenException("This token has been invalidated");

    if (!encoder.matches(loginInfo.password(), referredToken.getPassword()))
      throw new BadCredentialsException("Wrong password");

    try {
      ContractorNameDTO relatedContractorName = invitationTokenRepository
          .getRelatedContractorNameByTokenUrl(loginInfo.tokenUrl());
      String contractorFullName = relatedContractorName.firstName() + " " + relatedContractorName.lastName();

      AuthenticatedUserDetails newAuthData = new AuthenticatedUserDetails(referredToken.getContractorEmail(),
          contractorFullName, "", Roles.CONTRACTOR);
      PreAuthenticatedAuthenticationToken newAuth = new PreAuthenticatedAuthenticationToken(
          newAuthData,
          null);
      newAuth.setAuthenticated(true);
      SecurityContextHolder.getContext().setAuthentication(newAuth);

      String token = jwtService.generateToken(
          new JwtClaimsDTO(contractorFullName, Roles.CONTRACTOR, JwtTypes.contractorAuth),
          referredToken.getContractorEmail());
      return new TokenDTO(token, contractorFullName);
    } catch (Exception e) {
      throw new RuntimeException("Unknown error in authentication: " + e.getMessage());
    }
  }

  @Override
  public void invalidateToken(String tokenUrl) {
    InvitationTokens referredToken = invitationTokenRepository.findByTokenUrl(tokenUrl).orElseThrow(
        () -> new NotFoundException("Invitation with token '%s' not found".formatted(tokenUrl)));

    invitationTokenRepository.save(referredToken.toBuilder().isValid(false).build());
  }
}
