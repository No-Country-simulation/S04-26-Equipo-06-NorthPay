package org.northpay_contractor_onboarding.dto;

import lombok.Builder;

@Builder
public record MetricsResponseDTO(
  long totalOnboardings,
  long approvedOnboardings,
  long changesRequestedOnboardings,
  long notStartedOnboardings // con el status de Invited
) {}
