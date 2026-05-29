package org.northpay_contractor_onboarding.repository;

import org.northpay_contractor_onboarding.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {

    @Query("SELECT d FROM Document d WHERE d.onboarding.id = :onboardingId")
    Document findByOnboardingId(@Param("onboardingId") UUID onboardingId);

}
