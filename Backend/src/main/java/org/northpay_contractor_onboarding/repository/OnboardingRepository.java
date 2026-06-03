package org.northpay_contractor_onboarding.repository;

import java.util.Optional;
import java.util.UUID;

import org.northpay_contractor_onboarding.model.Onboarding;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.northpay_contractor_onboarding.enums.OnboardingStatus;


public interface OnboardingRepository extends JpaRepository<Onboarding, UUID> {
  @Query("select o from Onboarding o join o.contractor c where o.id = :onboardingId and c.contactInformation.email = :contractorEmail")
  Optional<Onboarding> findByIdAndContractorEmail(UUID onboardingId, String contractorEmail);

  @EntityGraph(attributePaths = {"contractor"})
  Page<Onboarding> findAll(Pageable pageable);

  long countByStatus(OnboardingStatus status);
}
