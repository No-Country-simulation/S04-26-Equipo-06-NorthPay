package org.northpay_contractor_onboarding.repository;

import org.northpay_contractor_onboarding.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ContractRepository  extends JpaRepository<Contract, UUID> {

    @Query("SELECT c FROM Contract c WHERE c.onboarding.id = :onboardingId")
    Contract findByOnboardingId(@Param("onboardingId") UUID onboardingId);

}
