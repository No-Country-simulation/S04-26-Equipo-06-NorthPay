package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.model.Onboarding;

public interface IOnboardingHistoryService {

    void saveOnboardingHistory(Onboarding onboarding , OnboardingStatus previousStatus);

}
