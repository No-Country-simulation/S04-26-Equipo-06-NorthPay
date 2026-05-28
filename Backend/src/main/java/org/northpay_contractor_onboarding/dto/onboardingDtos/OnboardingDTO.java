package org.northpay_contractor_onboarding.dto.onboardingDtos;

import java.time.LocalDateTime;
import java.util.UUID;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.enums.PaymentMethodTypes;
import org.northpay_contractor_onboarding.model.Onboarding;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OnboardingDTO {

    private UUID id;
    private Integer currentStep;
    private OnboardingStatus status;

    public OnboardingDTO(Onboarding onboarding) {
        this.currentStep = onboarding.getCurrentStep();
        this.id = onboarding.getId();
        this.status = onboarding.getStatus();

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
    }
}
