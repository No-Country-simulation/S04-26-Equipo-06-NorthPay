package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.onboardingDtos.RegistroHistoryDTO;
import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.exception.ConflictException;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StateMachineService {
    private final IOnboardingHistoryService iOnboardingHistoryService;
    private final MetricsService metricsService;

    @Transactional(propagation = Propagation.MANDATORY)
    public void transitionTo(Onboarding onboarding, OnboardingStatus nextStatus, String changedBy) {
        OnboardingStatus currentStatus = onboarding.getStatus();

        if (currentStatus == OnboardingStatus.APPROVED) {
            throw new ConflictException(
                    "No se pueden realizar modificaciones: El proceso de onboarding ya se encuentra APROBADO.");
        }

        if (!currentStatus.canTransitionTo(nextStatus)) {

            var registroHistoryDTO = RegistroHistoryDTO.builder()
                    .changedBy(changedBy)
                    .onboarding(onboarding)
                    .oldStatus(currentStatus)
                    .newStatus(nextStatus)
                    .reason("RECHAZADO: Transición inválida")
                    .type("INTENTO_FALLIDO")
                    .build();

            registerHistory(registroHistoryDTO);
            throw new ConflictException(
                    "Transición inválida: No se puede pasar de %s a %s".formatted(currentStatus, nextStatus));
        }

        onboarding.setStatus(nextStatus);
        var registroHistoryDTO = RegistroHistoryDTO.builder()
                .onboarding(onboarding)
                .changedBy(changedBy)
                .oldStatus(currentStatus)
                .newStatus(nextStatus)
                .reason("Transición exitosa")
                .type("EXITOSO")
                .build();

        registerHistory(registroHistoryDTO);
        metricsService.emitMetricsEvent();
    }

    private void registerHistory(RegistroHistoryDTO registroHistoryDTO) {

        iOnboardingHistoryService.saveOnboardingHistory(registroHistoryDTO);
    }

}
