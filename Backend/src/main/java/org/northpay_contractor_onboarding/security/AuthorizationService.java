package org.northpay_contractor_onboarding.security;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

import org.northpay_contractor_onboarding.enums.Roles;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.northpay_contractor_onboarding.security.authentication.AuthenticatedUserDetails;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class AuthorizationService {
  private final OnboardingRepository onboardingRepository;

  // Se fija si el contractor que está logueado, dentro del token JWT, está asociado al Onboarding objetivo del cambio
  public boolean contractorCanChangeOnboarding(String onboardingId) throws AccessDeniedException {
    Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
    if (existingAuth == null || !(existingAuth instanceof AuthenticatedUserDetails userDetails))
      throw new AuthenticationCredentialsNotFoundException("Not authenticated");
    if (userDetails.getRole().equals(Roles.OPERATOR))
      throw new AccessDeniedException("Invalid access. This operation can be done only by a contractor");

    String contractorEmail = userDetails.getEmail();
    return onboardingRepository.findByIdAndContractorEmail(UUID.fromString(onboardingId), contractorEmail).isPresent();
  }
}
