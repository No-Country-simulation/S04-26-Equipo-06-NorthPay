package org.northpay_contractor_onboarding.service;

import java.time.Duration;
import java.util.List;

import org.northpay_contractor_onboarding.dto.MetricsDTO;
import org.northpay_contractor_onboarding.dto.invTokensDtos.InvTokenActivationTimesDTO;
import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.events.events.MetricsEvent;
import org.northpay_contractor_onboarding.repository.InvitationTokenRepository;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetricsService {
  private final OnboardingRepository onboardingRepository;
  private final InvitationTokenRepository invitationTokenRepository;
  private final ApplicationEventPublisher eventPublisher;

  private MetricsDTO generateMetrics() {
    long averageTime = 0;
    if (!onboardingRepository.findAll().isEmpty()) {
      List<InvTokenActivationTimesDTO> times = invitationTokenRepository.getOnbCreationAndActivationTimes();
      long sumOfDifferencesBetweenTimes = times.isEmpty() ? 0 : times.stream().mapToLong(
        dto -> (dto.getActivatedAt() != null && dto.getCreatedAt() != null) ? Duration.between(dto.getCreatedAt(), dto.getActivatedAt()).getSeconds() : 0
      ).sum();

      averageTime = times.isEmpty() ? 0 : Math.round(sumOfDifferencesBetweenTimes / times.size());
    }

    return MetricsDTO.builder()
      .totalOnboardings(onboardingRepository.count())
      .approvedOnboardings(onboardingRepository.countByStatus(OnboardingStatus.APPROVED))
      .changesRequestedOnboardings(onboardingRepository.countByStatus(OnboardingStatus.CHANGES_REQUESTED))
      .notStartedOnboardings(onboardingRepository.countByStatus(OnboardingStatus.INVITED))
      .averageSecondsActivationTimeOfTokens(averageTime)
    .build();
  }

  @Transactional(readOnly = true)
  public MetricsDTO getMetrics() {
    return generateMetrics();
  }

  public void emitMetricsEvent() {
    eventPublisher.publishEvent(new MetricsEvent(generateMetrics()));
  }
}
