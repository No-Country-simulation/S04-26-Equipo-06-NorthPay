package org.northpay_contractor_onboarding.service;

import java.util.UUID;

import org.northpay_contractor_onboarding.dto.OnboardingDTO;


public interface IOnboardiIngService {

  OnboardingDTO savePersonalData(UUID onboardingId, OnboardingDTO.RequestOnboarding requestOnboarding);

}
