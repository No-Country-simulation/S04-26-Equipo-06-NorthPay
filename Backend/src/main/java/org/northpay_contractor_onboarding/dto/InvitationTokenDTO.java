package org.northpay_contractor_onboarding.dto;

import lombok.Builder;

@Builder
public record InvitationTokenDTO(
  String id,
  String tokenUrl,
  String contractorEmail,
  boolean used,
  boolean isValid,
  String expiresAt,
  String createdAt,
  String createdBy,
  String onboardingId
) {}
