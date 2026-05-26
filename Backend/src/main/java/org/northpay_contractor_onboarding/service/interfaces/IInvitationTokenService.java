package org.northpay_contractor_onboarding.service.interfaces;

import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.InvitationTokenDTO;
import org.northpay_contractor_onboarding.dto.authentication.ContractorLoginDTO;
import org.northpay_contractor_onboarding.dto.authentication.InvTokenContractorSignUp;
import org.northpay_contractor_onboarding.dto.jwt.TokenDTO;
import org.northpay_contractor_onboarding.security.authentication.AuthenticatedUserDetails;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface IInvitationTokenService {
  List<InvitationTokenDTO> getAll();

  public InvitationTokenDTO create(UUID onboardingId, String contractorEmail, AuthenticatedUserDetails loggedOperator);

  public boolean checkInvitationTokenUrlIsExpired(String tokenUrl);

  public void useTokenForFirstTime(InvTokenContractorSignUp info);

  TokenDTO login(@Valid ContractorLoginDTO loginInfo);

  void invalidateToken(@NotBlank String tokenUrl);

  InvitationTokenDTO validateAndGetTokenData(String tokenUrl);
}
