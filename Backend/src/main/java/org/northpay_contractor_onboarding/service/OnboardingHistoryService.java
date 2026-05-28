package org.northpay_contractor_onboarding.service;

import java.time.LocalDateTime;

import org.northpay_contractor_onboarding.dto.onboardingDtos.RegistroHistoryDTO;

import org.northpay_contractor_onboarding.model.OnboardingHistory;
import org.northpay_contractor_onboarding.repository.OnboardingHistoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OnboardingHistoryService implements IOnboardingHistoryService {

    private final OnboardingHistoryRepository onboardingHistoryRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOnboardingHistory(RegistroHistoryDTO registroHistoryDTO
    ) {

        var onboardingHistory = OnboardingHistory.builder()
                .onboarding(registroHistoryDTO.getOnboarding())
                .newStatus(registroHistoryDTO.getNewStatus())
                .oldStatus(registroHistoryDTO.getOldStatus())
                .changedBy(registroHistoryDTO.getChangedBy())
                .changedAt(LocalDateTime.now())
                .reason(registroHistoryDTO.getReason())
                .build();

        onboardingHistoryRepository.save(onboardingHistory);

    }

}
