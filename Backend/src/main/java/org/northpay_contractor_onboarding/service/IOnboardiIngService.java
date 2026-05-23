package org.northpay_contractor_onboarding.service;

import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.onboardingDtos.DataPersonalDTO;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingApproveRequest;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingChangeRequested;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingCompleteDTO;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingDTO;

import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingSummaryDTO;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IOnboardiIngService {

  OnboardingDTO savePersonalData(UUID onboardingId, OnboardingDTO.RequestOnboarding requestOnboarding);
  Onboarding create();
  OnboardingDTO finalizeOnboarding(UUID id);
  OnboardingDTO update(UUID id , Onboarding onboarding);
  OnboardingDTO getOnboarding(UUID id);
  OnboardingCompleteDTO getOnboardinAmin(UUID id);
  Page<OnboardingSummaryDTO> getAll(Pageable pageable);
  DataPersonalDTO dataPersonal(UUID id);
  OnboardingDTO approve(UUID id, OnboardingApproveRequest responseOnboardig);
  OnboardingDTO changeRequested(UUID id, OnboardingChangeRequested responseOnboardig);
}
