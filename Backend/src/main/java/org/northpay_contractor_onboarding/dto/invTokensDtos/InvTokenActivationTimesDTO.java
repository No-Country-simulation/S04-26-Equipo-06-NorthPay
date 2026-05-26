package org.northpay_contractor_onboarding.dto.invTokensDtos;

import java.time.LocalDateTime;

public interface InvTokenActivationTimesDTO {
  LocalDateTime getCreatedAt(); // creation time of onboarding
  LocalDateTime getActivatedAt(); // activation time of Invitation token
}
