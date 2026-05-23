package org.northpay_contractor_onboarding.controller;

import java.util.Map;
import java.util.UUID;

import org.northpay_contractor_onboarding.enums.PaymentMethodTypes;
import org.northpay_contractor_onboarding.exception.NotFoundException;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.northpay_contractor_onboarding.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/onboarding")
@AllArgsConstructor
public class AuditLogController {
    private final AuditLogService auditService;
    private final OnboardingRepository onboardingRepository;

    @PostMapping("/{id}/reveal")
    public ResponseEntity<Map<String, String>> revealSensitiveData(
            @PathVariable UUID id,
            @RequestParam String fieldName) {

        var onboarding = onboardingRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Onboarding not found"));

        String cleanValue = "N/A";
        var contractor = onboarding.getContractor();

        if (contractor != null) {
            if ("email".equalsIgnoreCase(fieldName)) {
                cleanValue = contractor.getEmail();
            } else if ("phone".equalsIgnoreCase(fieldName)) {
                cleanValue = contractor.getContactInformation() != null
                        ? contractor.getContactInformation().getPhoneNumber()
                        : "N/A";
            } else if ("documentNumber".equalsIgnoreCase(fieldName)) {

                cleanValue = "documentNumber";

            } else if ("account".equalsIgnoreCase(fieldName)) {
                var payment = onboarding.getPaymentMethod();

                if (payment != null && payment.getPaymentMethodType() != null) {

                    if (payment.getPaymentMethodType() == PaymentMethodTypes.DIGITAL_PLATFORM) {
                        cleanValue = payment.getWalletEmail();
                    }

                    else if (payment.getPaymentMethodType() == PaymentMethodTypes.CRYPTO_CURRENCY) {
                        cleanValue = payment.getWalletAddress();
                    }
                }
            }
        }

        auditService.logAction("Operator", "REVEAL_DATA", "Field: " + fieldName + " on Onboarding " + id, "SUCCESS");

        return ResponseEntity.ok(Map.of("revealedValue", cleanValue));
    }

}
