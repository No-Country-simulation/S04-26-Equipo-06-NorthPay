package org.northpay_contractor_onboarding.service.interfaces;

import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.InvitationTokenDTO;
import org.northpay_contractor_onboarding.dto.authentication.ContractorLoginDTO;
import org.northpay_contractor_onboarding.dto.authentication.InvTokenContractorSignUp;
import org.northpay_contractor_onboarding.dto.jwt.TokenDTO;
import org.northpay_contractor_onboarding.security.authentication.AuthenticatedUserDetails;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface IInvitationTokenService {
  List<InvitationTokenDTO> getAll();
  InvitationTokenDTO create(@NotNull UUID onboardingId, @NotNull String contractorEmail, AuthenticatedUserDetails authOperator);

  /**
   * 
   * @param token el token que viene desde la url
   * @return true si el token está expirado
   */
  boolean checkInvitationTokenUrlIsExpired(String tokenUrl);

  void useTokenForFirstTime(@Valid InvTokenContractorSignUp data);

  TokenDTO login(@Valid ContractorLoginDTO loginInfo);
}
