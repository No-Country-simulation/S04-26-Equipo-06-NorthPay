package org.northpay_contractor_onboarding.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.OnboardingDTO;
import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.exception.NotFoundException;

import org.northpay_contractor_onboarding.model.Contractor;

import org.northpay_contractor_onboarding.repository.OnboardingRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OnboardingService implements IOnboardiIngService {

        private final OnboardingRepository onboardingRepository;
        private final IOnboardingHistoryService onboardingHistoryService;
        private final IContractorService ioContractorService;

        @Override
        @Transactional
        public OnboardingDTO savePersonalData(UUID id, OnboardingDTO.RequestOnboarding requestOnboarding) {

                var onboarding = onboardingRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("Onboarding not found"));

                if (onboarding.getStatus() != OnboardingStatus.INVITED) {
                        throw new IllegalStateException(
                                        "Onboarding is not in the correct status to update personal data");
                }

                Contractor dbContractor = ioContractorService.saveContractor(requestOnboarding);

                OnboardingStatus previousStatus = onboarding.getStatus();
                onboarding.setContractor(dbContractor);
                onboarding.setStatus(OnboardingStatus.PERSONAL_DATA_COMPLETED);
                onboarding.setCurrentStep(2);
                onboarding.setUpdatedAt(LocalDateTime.now());

                var dbOnboarding = onboardingRepository.save(onboarding);

                onboardingHistoryService.saveOnboardingHistory(dbOnboarding, previousStatus);

                OnboardingDTO onboardingDTO = new OnboardingDTO();
                onboardingDTO.setCurrentStep(dbOnboarding.getCurrentStep());
                onboardingDTO.setId(dbOnboarding.getId());
                onboardingDTO.setStatus(dbOnboarding.getStatus());

                return onboardingDTO;

        }

}
