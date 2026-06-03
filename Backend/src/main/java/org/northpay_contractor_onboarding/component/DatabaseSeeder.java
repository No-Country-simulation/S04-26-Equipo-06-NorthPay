package org.northpay_contractor_onboarding.component;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.model.ContactInformation;
import org.northpay_contractor_onboarding.model.Contractor;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.northpay_contractor_onboarding.repository.InvitationTokenRepository;
import org.northpay_contractor_onboarding.model.InvitationTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.ZoneId;
import java.util.UUID;

import java.time.LocalDateTime;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private OnboardingRepository onboardingRepository;

    @Autowired
    private InvitationTokenRepository invitationTokenRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            if (onboardingRepository.count() < 5) {
                System.out.println("Seeding Onboardings...");

                seedOnboarding("Mateo", "Pérez", "mateo.perez@ejemplo.com.ar", OnboardingStatus.INVITED, 1);
                seedOnboarding("Sofía", "Gómez", "sofia.gomez@ejemplo.com.ar", OnboardingStatus.DOCUMENTS_UPLOADED, 3);
                seedOnboarding("Facundo", "Martínez", "facundo.martinez@ejemplo.com.ar", OnboardingStatus.PENDING_VERIFICATION, 6);
                seedOnboarding("Camila", "Rodríguez", "camila.rodriguez@ejemplo.com.ar", OnboardingStatus.CHANGES_REQUESTED, 6);
                seedOnboarding("Valentina", "López", "valentina.lopez@ejemplo.com.ar", OnboardingStatus.APPROVED, 6);

                System.out.println("Seeding completed.");
            }
        } catch (Exception e) {
            System.err.println("Seeding failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void seedOnboarding(String firstName, String lastName, String email, OnboardingStatus status, int currentStep) {
        Contractor contractor = Contractor.builder()
                .firstName(firstName)
                .lastName(lastName)
                .contactInformation(new ContactInformation(email, "Argentina", "+541112345678", "Av. Corrientes 1234"))
                .createdAt(LocalDateTime.now().minusDays((long) (Math.random() * 30)))
                .build();        

        Onboarding onboarding = Onboarding.builder()
                .contractor(contractor)
                .status(status)
                .currentStep(currentStep)
                .createdAt(LocalDateTime.now().minusDays((long) (Math.random() * 30)))
                .updatedAt(LocalDateTime.now())
                .build();

        onboardingRepository.save(onboarding);

        if (status != OnboardingStatus.INVITED) {
            long randomMinutes = 5 + (long)(Math.random() * 120); // between 5 and 125 minutes
            LocalDateTime activatedTime = onboarding.getCreatedAt().plusMinutes(randomMinutes);
            
            InvitationTokens token = InvitationTokens.builder()
                .tokenUrl(UUID.randomUUID().toString())
                .used(true)
                .isValid(true)
                .contractorEmail(email)
                .password("seeded_password")
                .operatorEmail("admin@northpay.com")
                .createdAt(onboarding.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant())
                .activatedAt(activatedTime)
                .expiresAt(onboarding.getCreatedAt().plusDays(7))
                .onboarding(onboarding)
                .build();
                
            invitationTokenRepository.save(token);
        }
    }
}
