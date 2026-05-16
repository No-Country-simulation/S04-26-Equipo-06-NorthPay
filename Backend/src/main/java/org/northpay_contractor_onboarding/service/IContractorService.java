package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.OnboardingDTO;
import org.northpay_contractor_onboarding.model.Contractor;

public interface IContractorService {

    Contractor saveContractor(OnboardingDTO.RequestOnboarding requestOnboarding , String email);

}
