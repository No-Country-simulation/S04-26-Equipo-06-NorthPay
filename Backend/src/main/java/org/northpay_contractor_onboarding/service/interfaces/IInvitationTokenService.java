package org.northpay_contractor_onboarding.service.interfaces;

import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.InvitationTokenDTO;
import org.northpay_contractor_onboarding.model.InvitationTokens;

public interface IInvitationTokenService {
  List<InvitationTokenDTO> getAll();
  InvitationTokens create(UUID onboardingId, String contractorEmail);
  /**
   * 
   * @param token el token que viene desde la url
   * @return true si el token está expirado
   */
  boolean checkInvitationTokenIsExpired(String token);
  void setToUsed(String token);
}
