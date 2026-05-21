package org.northpay_contractor_onboarding.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.onboardingDtos.DataPersonalDTO;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingApproveRequest;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingDTO;
import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.exception.NotFoundException;

import org.northpay_contractor_onboarding.model.Contractor;
import org.northpay_contractor_onboarding.model.Onboarding;

import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OnboardingService implements IOnboardiIngService {

        private final OnboardingRepository onboardingRepository;

        private final IContractorService ioContractorService;
        private final StateMachineService stateMachineService;

        @Override
        @Transactional
        public OnboardingDTO savePersonalData(UUID id, OnboardingDTO.RequestOnboarding requestOnboarding) {

                var onboarding = onboardingRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("Onboarding not found"));

                // TODO lo comento hasta que podamos hacer pruebas con el token de invitacion
                // if (!onboarding.getContractor().getEmail().contains(emailLogueado)) {
                // throw new
                // org.northpay_contractor_onboarding.exception.AccessDeniedException("No tenés
                // permiso para editar este onboarding.", HttpStatus.FORBIDDEN);
                // }

                Contractor dbContractor = ioContractorService.saveContractor(requestOnboarding,
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

                 var dbOnboardin = onboardingRepository.save(onboarding);

                return new OnboardingDTO(dbOnboardin );
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
        public Page<OnboardingDTO> getAll(Pageable pageable) {

                Page<Onboarding> onboardingPage = onboardingRepository.findAll(pageable);

                if (onboardingPage == null) {
                        throw new NotFoundException("there are no onboardings");
                }

                Page<OnboardingDTO> onboardingDTOs = onboardingPage.map(
                                onboar -> new OnboardingDTO(onboar));
                return onboardingDTOs;
        }

        @Override
        @Transactional(readOnly = true)
        public DataPersonalDTO dataPersonal(UUID id) {

                var dbOnboarding = onboardingRepository.findById(id).orElseThrow(
                                () -> new NotFoundException("onboarding not found"));

                return new DataPersonalDTO(dbOnboarding);

        }

        @Override
        public OnboardingDTO approve(UUID id, OnboardingApproveRequest responseOnboardig) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'approve'");
        }

}
