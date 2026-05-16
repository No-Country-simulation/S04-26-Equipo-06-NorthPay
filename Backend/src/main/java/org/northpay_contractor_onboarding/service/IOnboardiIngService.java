package org.northpay_contractor_onboarding.service;

import java.util.UUID;

import org.northpay_contractor_onboarding.dto.OnboardingDTO;
import org.northpay_contractor_onboarding.model.Onboarding;


public interface IOnboardiIngService {

  OnboardingDTO savePersonalData(UUID onboardingId, OnboardingDTO.RequestOnboarding requestOnboarding);
  Onboarding create();
  OnboardingDTO finalizeOnboarding(UUID id);
}
