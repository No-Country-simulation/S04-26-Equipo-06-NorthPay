package org.northpay_contractor_onboarding.service;

import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.OnboardingDTO;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IOnboardiIngService {

  OnboardingDTO savePersonalData(UUID onboardingId, OnboardingDTO.RequestOnboarding requestOnboarding);
  Onboarding create();
  OnboardingDTO finalizeOnboarding(UUID id);
  OnboardingDTO update(UUID id , Onboarding onboarding);
  OnboardingDTO getOnboarding(UUID id);
Page<OnboardingDTO> getAll(Pageable pageable);
}
