package org.northpay_contractor_onboarding.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.ContractorNameDTO;
import org.northpay_contractor_onboarding.dto.invTokensDtos.InvTokenActivationTimesDTO;
import org.northpay_contractor_onboarding.model.InvitationTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvitationTokenRepository extends JpaRepository<InvitationTokens, UUID> {
  Optional<InvitationTokens> findByTokenUrl(String tokenUrl);

  Optional<InvitationTokens> findFirstByOnboardingIdOrderByCreatedAtDesc(UUID onboardingId);

  @Query("select invToken.onboarding.contractor.firstName, invToken.onboarding.contractor.lastName from InvitationTokens invToken where invToken.tokenUrl = :tokenUrl")
  ContractorNameDTO getRelatedContractorNameByTokenUrl(String tokenUrl);

  @Query("""
    select
      invToken.onboarding.createdAt as createdAt,
      invToken.activatedAt as activatedAt
    from InvitationTokens invToken
    where invToken.isValid = true
  """)
  List<InvTokenActivationTimesDTO> getOnbCreationAndActivationTimes();
}
