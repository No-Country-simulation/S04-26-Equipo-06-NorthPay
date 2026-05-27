package org.northpay_contractor_onboarding.dto.resendMailer;

public record ResendAPIResponseDTO(
  String id,
  String object,
  String status,
  String to,
  String subject,
  String html,
  String from,
  String createdAt
) {}
