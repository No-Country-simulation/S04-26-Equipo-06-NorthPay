package org.northpay_contractor_onboarding.service;

import java.time.LocalDateTime;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.model.OnboardingHistory;
import org.northpay_contractor_onboarding.repository.OnboardingHistoryRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OnboardingHistoryService implements IOnboardingHistoryService {

    private final OnboardingHistoryRepository onboardingHistoryRepository;

    @Override
    public void saveOnboardingHistory(Onboarding onboarding, OnboardingStatus previousStatus) {

        var onboardingHistory = OnboardingHistory.builder()
                .onboarding(onboarding)
                .newStatus(onboarding.getStatus())
                .oldStatus(previousStatus)
                .changedBy(onboarding.getContractor().getFirstName())
                .changedAt(LocalDateTime.now())
                .build();

        onboardingHistoryRepository.save(onboardingHistory);

    }

}
