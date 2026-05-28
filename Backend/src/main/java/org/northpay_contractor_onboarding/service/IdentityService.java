package org.northpay_contractor_onboarding.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.IdentityValidationResponseDTO;
import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.exception.NotFoundException;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class IdentityService {

    @Autowired
    private OnboardingRepository onboardingRepository;

    @Autowired
    private StateMachineService stateMachineService;

    @Transactional(rollbackFor = Exception.class)
    public IdentityValidationResponseDTO validateIdentity(UUID onboardingId, MultipartFile selfieFile) {

        Onboarding onboarding = onboardingRepository.findById(onboardingId)
                .orElseThrow(() -> new NotFoundException("Onboarding not found"));

        try {

            String fileName = UUID.randomUUID().toString() + "_" + selfieFile.getOriginalFilename();
            Path path = Paths.get("uploads/selfies");
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Files.copy(selfieFile.getInputStream(), path.resolve(fileName));

            Thread.sleep(2000);
            if (onboarding.getStatus() == OnboardingStatus.PAYMENT_CONFIGURED) {
                stateMachineService.transitionTo(onboarding, OnboardingStatus.IDENTITY_VERIFICATION_COMPLETED,
                        "CONTRACTOR");
            }
            if(onboarding.getCurrentStep() == 5){
                onboarding.setCurrentStep(6);
            }

            var dbOnboarding = onboardingRepository.save(onboarding);

            return IdentityValidationResponseDTO.builder()
                    .status("SUCCESS")
                    .score(0.98)
                    .message("Identity successfully verified through NorthPay biometric system.")
                    .Onboardingid(dbOnboarding.getId())

                    .build();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error in biometric simulation");
        } catch (IOException e) {
            throw new RuntimeException("Error saving ID photo");
        }
    }
}
