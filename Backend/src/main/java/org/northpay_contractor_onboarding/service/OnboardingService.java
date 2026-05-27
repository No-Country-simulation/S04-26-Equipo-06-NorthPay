package org.northpay_contractor_onboarding.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.onboardingDtos.DataPersonalDTO;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingApproveRequest;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingChangeRequested;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingCompleteDTO;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingDTO;

import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingSummaryDTO;
import org.northpay_contractor_onboarding.enums.ApprovalStatus;
import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.exception.NotFoundException;

import org.northpay_contractor_onboarding.model.Contractor;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.model.OnboardingReview;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.northpay_contractor_onboarding.repository.OnboardingReviewRepository;
import org.northpay_contractor_onboarding.security.authentication.AuthenticatedUserDetails;
import org.northpay_contractor_onboarding.service.interfaces.IInvitationTokenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OnboardingService implements IOnboardiIngService {

        private final OnboardingRepository onboardingRepository;
        private final AuditLogService auditLogService;
        private final IContractorService ioContractorService;
        private final IInvitationTokenService invitationTokenService;
        private final StateMachineService stateMachineService;
        private final MetricsService metricsService;
        private final OnboardingReviewRepository onboardingReviewRepository;

        @Override
        @Transactional
        public OnboardingDTO savePersonalData(UUID id, OnboardingDTO.RequestOnboarding requestOnboarding) {

                var onboarding = onboardingRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("Onboarding not found"));

                Contractor dbContractor = ioContractorService.saveContractor(
                                onboarding.getContractor().getId(),
                                requestOnboarding,
                                requestOnboarding.getEmail());

                onboarding.setContractor(dbContractor);
                onboarding.setUpdatedAt(LocalDateTime.now());

                stateMachineService.transitionTo(onboarding, OnboardingStatus.PERSONAL_DATA_COMPLETED, "USER");
                onboarding.setCurrentStep(2);

                var dbOnboarding = onboardingRepository.save(onboarding);

                OnboardingDTO onboardingDTO = new OnboardingDTO(dbOnboarding);

                return onboardingDTO;

        }

        @Override
        @Transactional
        public Onboarding create(String destinedContractorEmail, AuthenticatedUserDetails loggedOperator) {

                Onboarding onboarding = Onboarding.builder()
                                .createdAt(LocalDateTime.now())
                                .currentStep(1)
                                .status(OnboardingStatus.INVITED)
                                .contractor(ioContractorService.saveContractor(
                                                null,
                                                OnboardingDTO.RequestOnboarding.builder().email(destinedContractorEmail)
                                                                .build(),
                                                destinedContractorEmail))
                                .build();

                var dbOnboarding = onboardingRepository.save(onboarding);

                metricsService.emitMetricsEvent();
                invitationTokenService.create(dbOnboarding.getId(), destinedContractorEmail, loggedOperator);

                return dbOnboarding;
        }

        @Override
        @Transactional
        public OnboardingDTO finalizeOnboarding(UUID id) {

                var onboarding = onboardingRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("Onboarding not found"));

                if (onboarding.getStatus() == OnboardingStatus.IDENTITY_VERIFICATION_COMPLETED) {
                        stateMachineService.transitionTo(onboarding, OnboardingStatus.PENDING_VERIFICATION, "USER");
                        onboarding.setCurrentStep(6);
                }

                var dbOnboarding = onboardingRepository.save(onboarding);

                return new OnboardingDTO(dbOnboarding);
        }

        @Override
        @Transactional
        public OnboardingDTO update(UUID id, Onboarding responseOnboarding) {

                var onboarding = onboardingRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("Onboarding not found"));

                onboarding.setStatus(responseOnboarding.getStatus());
                metricsService.emitMetricsEvent();

                var dbOnboardin = onboardingRepository.save(onboarding);

                return new OnboardingDTO(dbOnboardin);
        }

        @Override
        @Transactional(readOnly = true)
        public OnboardingDTO getOnboarding(UUID id) {
                var dbOnboarding = onboardingRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("no se encontro el onboarding"));

                return new OnboardingDTO(dbOnboarding);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<OnboardingSummaryDTO> getAll(Pageable pageable) {

                Page<Onboarding> onboardingPage = onboardingRepository.findAll(pageable);

                if (onboardingPage == null) {
                        throw new NotFoundException("there are no onboardings");
                }

                Page<OnboardingSummaryDTO> onboardingSummaryDTOs = onboardingPage.map(o -> {

                        ApprovalStatus visualStatus = ApprovalStatus.fromOnboardingStatus(o.getStatus());
                        return new OnboardingSummaryDTO(visualStatus, o);
                });

                return onboardingSummaryDTOs;
        }

        @Override
        @Transactional(readOnly = true)
        public DataPersonalDTO dataPersonal(UUID id) {

                var dbOnboarding = onboardingRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("onboarding not found"));

                var contactInformation = dbOnboarding.getContractor().getContactInformation();

                return new DataPersonalDTO(dbOnboarding, contactInformation);

        }

        @Override
        @Transactional
        public OnboardingDTO approve(UUID id, OnboardingApproveRequest responseOnboardig) {

                var onboardingDb = onboardingRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("onboarding not found"));

                OnboardingReview review = new OnboardingReview();
                review.setOnboardingStatus(OnboardingStatus.APPROVED);
                review.setReason(responseOnboardig.getReason());
                review.setOnboarding(onboardingDb);
                onboardingReviewRepository.save(review);

                stateMachineService.transitionTo(onboardingDb, OnboardingStatus.APPROVED, "Operator");
                var dbOnboarding = onboardingRepository.save(onboardingDb);

                // TODO disparamos notificacion

                return new OnboardingDTO(dbOnboarding);
        }

        @Override
        @Transactional
        public OnboardingDTO changeRequested(UUID id, OnboardingChangeRequested responseOnboardig) {

                var onboardingDb = onboardingRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("onboarding not found"));

                OnboardingReview review = new OnboardingReview();
                review.setOnboardingStatus(OnboardingStatus.CHANGES_REQUESTED);
                review.setReason(responseOnboardig.getReason());
                review.setOnboarding(onboardingDb);
                onboardingReviewRepository.save(review);
                stateMachineService.transitionTo(onboardingDb, OnboardingStatus.CHANGES_REQUESTED, "Operator");
                    

                 var dbOnboarding = onboardingRepository.save(onboardingDb);

                // TODO disparamos notificacion

                return new OnboardingDTO(dbOnboarding);
        }

        @Override
        @Transactional(readOnly = true)
        public OnboardingCompleteDTO getOnboardinAmin(UUID id) {

                var onboardinFull = onboardingRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("onboarding not found"));

                auditLogService.logAction("Operator", "VIEW_ONBOARDING_DETAIL", "Onboarding ID: " + id, "SUCCESS");

                return new OnboardingCompleteDTO(onboardinFull);

        }

}
