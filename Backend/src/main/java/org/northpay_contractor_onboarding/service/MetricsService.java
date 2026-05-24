package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.MetricsResponseDTO;
import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.events.events.MetricsEvent;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetricsService {
  private final OnboardingRepository onboardingRepository;
  private final ApplicationEventPublisher eventPublisher;

  private MetricsResponseDTO generateMetrics() {
    return MetricsResponseDTO.builder()
      .totalOnboardings(onboardingRepository.count())
      .approvedOnboardings(onboardingRepository.countByStatus(OnboardingStatus.APPROVED))
      .changesRequestedOnboardings(onboardingRepository.countByStatus(OnboardingStatus.CHANGES_REQUESTED))
      .notStartedOnboardings(onboardingRepository.countByStatus(OnboardingStatus.INVITED))
    .build();
  }

  @Transactional(readOnly = true)
  public MetricsResponseDTO getMetrics() {
    return generateMetrics();
  }

  public void emitMetricsEvent() {
    eventPublisher.publishEvent(new MetricsEvent(getMetrics()));
  }
}
