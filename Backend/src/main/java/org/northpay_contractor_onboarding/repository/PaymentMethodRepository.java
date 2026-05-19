package org.northpay_contractor_onboarding.repository;

import org.northpay_contractor_onboarding.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {

    @Query("SELECT p FROM PaymentMethod p WHERE p.onboarding.id = :onboarding_id")
    PaymentMethod findByOnboardingId(@Param("onboarding_id")UUID onboarding_id);

}
