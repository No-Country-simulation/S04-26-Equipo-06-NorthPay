package org.northpay_contractor_onboarding.service.interfaces;

import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.InvitationTokenDTO;
import org.northpay_contractor_onboarding.dto.authentication.ContractorLoginDTO;
import org.northpay_contractor_onboarding.dto.authentication.InvTokenContractorSignUp;
import org.northpay_contractor_onboarding.dto.jwt.TokenDTO;
import org.northpay_contractor_onboarding.security.authentication.AuthenticatedUserDetails;

public interface IInvitationTokenService {
  List<InvitationTokenDTO> getAll();
  InvitationTokenDTO create(UUID onboardingId, String contractorEmail, AuthenticatedUserDetails authOperator);

  /**
   * 
   * @param token el token que viene desde la url
   * @return true si el token está expirado
   */
  boolean checkInvitationTokenUrlIsExpired(String tokenUrl);

  TokenDTO useTokenForFirstTime(InvTokenContractorSignUp data);

  TokenDTO login(ContractorLoginDTO loginInfo);
}
