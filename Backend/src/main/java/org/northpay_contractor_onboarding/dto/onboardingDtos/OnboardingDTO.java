package org.northpay_contractor_onboarding.dto.onboardingDtos;

import java.time.LocalDateTime;
import java.util.UUID;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.model.Onboarding;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OnboardingDTO {

    private UUID id;
    private Integer currentStep;
    private OnboardingStatus status;
    private RequestOnboarding personalData;
    private RequestDocument documentData;
    private RequestContract contractData;
    private RequestPayment paymentData;

    public OnboardingDTO(Onboarding onboarding) {
        this.currentStep = onboarding.getCurrentStep();
        this.id = onboarding.getId();
        this.status = onboarding.getStatus();

        if (onboarding.getContractor() != null) {
            String firstName = onboarding.getContractor().getFirstName();
            String lastName = onboarding.getContractor().getLastName();
            this.personalData = RequestOnboarding.builder()
                .name(firstName == null && lastName == null ? "Guest" : firstName)
                .lastName(firstName == null && lastName == null ? "" : lastName)
                .email(onboarding.getContractor().getEmail())
                .phoneNumber(onboarding.getContractor().getContactInformation() != null ? onboarding.getContractor().getContactInformation().getPhoneNumber() : null)
                .country(onboarding.getContractor().getContactInformation() != null ? onboarding.getContractor().getContactInformation().getCountry() : null)
                .address(onboarding.getContractor().getContactInformation() != null ? onboarding.getContractor().getContactInformation().getAddress() : null)
                .dniNumber(onboarding.getContractor().getTextId())
                .verificationNotes(onboarding.getContractor().getVerificationNotes())
                .build();
        }

        if (onboarding.getDocuments() != null && !onboarding.getDocuments().isEmpty()) {
            var doc = onboarding.getDocuments().get(0);
            this.documentData = RequestDocument.builder()
                .documentName(doc.getFileUrl())
                .documentType(doc.getFileType())
                .dniNumber(onboarding.getContractor() != null ? onboarding.getContractor().getTextId() : null)
                .build();
        }

        if (onboarding.getContract() != null) {
            this.contractData = RequestContract.builder()
                .contractAccepted(Boolean.TRUE.equals(onboarding.getContract().getSigned()))
                .build();
        }

        if (onboarding.getPaymentMethod() != null) {
            var pm = onboarding.getPaymentMethod();
            this.paymentData = RequestPayment.builder()
                .paymentMethod(pm.getPaymentMethodType() != null ? pm.getPaymentMethodType().name() : null)
                .platform(pm.getPlatform())
                .walletEmail(pm.getWalletEmail())
                .network(pm.getNetwork())
                .walletAddress(pm.getWalletAddress())
                .isPaymentVerified(Boolean.TRUE.equals(pm.getIsPaymentVerified()))
                .verificationNotes(onboarding.getContractor() != null && onboarding.getContractor().getVerificationNotes() != null ? onboarding.getContractor().getVerificationNotes() : pm.getVerificationNotes())
                .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestOnboarding {
        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 3, max = 100)
        private String name;
        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 3, max = 100)
        private String lastName;
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Formato de email inválido")
        private String email;
        @NotBlank(message = "El teléfono es obligatorio")
        private String phoneNumber;
        @NotBlank(message = "El país es obligatorio")
        private String country;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt;
        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 255)
        private String address;
        private String dniNumber;
        private String verificationNotes;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestDocument {
        private String documentName;
        private String documentType;
        private String dniNumber;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestContract {
        private Boolean contractAccepted;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestPayment {
        private String paymentMethod;
        private String platform;
        private String walletEmail;
        private String network;
        private String walletAddress;
        private Boolean isPaymentVerified;
        private String verificationNotes;
    }
}
