package org.northpay_contractor_onboarding.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.OnboardingDTO;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.exception.NotFoundException;

import org.northpay_contractor_onboarding.model.Contractor;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.repository.InvitationTokenRepository;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.northpay_contractor_onboarding.repository.PaymentMethodRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OnboardingService implements IOnboardiIngService {

        private final OnboardingRepository onboardingRepository;
        private final IOnboardingHistoryService onboardingHistoryService;
        private final IContractorService ioContractorService;
        private final PaymentMethodRepository paymentMethodRepository;
        private final InvitationTokenRepository invitationTokenRepository;

        @Override
        @Transactional
        public OnboardingDTO savePersonalData(UUID id, OnboardingDTO.RequestOnboarding requestOnboarding) {

                String emailLogueado = SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getName();

                var onboarding = onboardingRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("Onboarding not found"));

                if (!onboarding.getContractor().getEmail().contains(emailLogueado)) {
                        throw new AccessDeniedException("No tenés permiso para editar este onboarding.");
                }

                if (!isReadyForPersonalData(onboarding)) {
                        throw new IllegalStateException(
                                        "Onboarding is not in the correct status to update personal data");
                }

                Contractor dbContractor = ioContractorService.saveContractor(requestOnboarding, emailLogueado);

                OnboardingStatus previousStatus = onboarding.getStatus();
                onboarding.setContractor(dbContractor);
                onboarding.setStatus(OnboardingStatus.PERSONAL_DATA_COMPLETED);
                onboarding.setCurrentStep(2);
                onboarding.setUpdatedAt(LocalDateTime.now());

                var dbOnboarding = onboardingRepository.save(onboarding);

                onboardingHistoryService.saveOnboardingHistory(dbOnboarding, previousStatus);

                OnboardingDTO onboardingDTO = new OnboardingDTO(dbOnboarding);

                return onboardingDTO;

        }

        @Override
        @Transactional
        public Onboarding create() {

                Onboarding onboarding = Onboarding.builder().createdAt(LocalDateTime.now())
                                .currentStep(1).status(OnboardingStatus.INVITED).build();


                var dbOnboarding = onboardingRepository.save(onboarding);

                return dbOnboarding;
        }

        @Override
        @Transactional
        public OnboardingDTO finalizeOnboarding(UUID id) {

                var onboarding = onboardingRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("Onboarding not found"));

                if (!isReadyForComplete(onboarding)) {
                        throw new IllegalStateException(
                                        "Onboarding is not in the correct status");
                }

                onboarding.setStatus(OnboardingStatus.PENDING_VERIFICATION);
                onboarding.setCreatedAt(LocalDateTime.now());
                onboarding.setCurrentStep(5);

                var dbOnboarding = onboardingRepository.save(onboarding);

                return new OnboardingDTO(dbOnboarding);
        }

 
        private boolean isReadyForPersonalData(Onboarding onboarding) {
                return onboarding.getStatus() == OnboardingStatus.INVITED;
        }

        private boolean isReadyForComplete(Onboarding onboarding) {
                return onboarding.getStatus() == OnboardingStatus.PENDING_VERIFICATION;
        }

}
