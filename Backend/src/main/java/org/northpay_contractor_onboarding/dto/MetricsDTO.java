package org.northpay_contractor_onboarding.dto;

import lombok.Builder;

@Builder
public record MetricsDTO(
  long totalOnboardings,
  long approvedOnboardings,
  long changesRequestedOnboardings,
  long notStartedOnboardings, // con el status de Invited
  long averageSecondsActivationTimeOfTokens
) {}
