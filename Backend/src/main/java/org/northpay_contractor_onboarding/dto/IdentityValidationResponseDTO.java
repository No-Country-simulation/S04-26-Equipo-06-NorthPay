package org.northpay_contractor_onboarding.dto;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class IdentityValidationResponseDTO {
    private String status;       // "SUCCESS" o "APPROVED"
    private double score;        // Metemos un número fake (ej: 0.98) para simular confianza de la IA
    private String message;      // "Identidad validada con éxito (Simulado)"
}
