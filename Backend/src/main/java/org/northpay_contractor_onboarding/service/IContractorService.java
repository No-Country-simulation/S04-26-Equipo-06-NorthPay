package org.northpay_contractor_onboarding.service;

import java.util.UUID;

import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingDTO;
import org.northpay_contractor_onboarding.model.Contractor;

public interface IContractorService {

    Contractor saveContractor(UUID contractorId, OnboardingDTO.RequestOnboarding requestOnboarding , String email);

}
