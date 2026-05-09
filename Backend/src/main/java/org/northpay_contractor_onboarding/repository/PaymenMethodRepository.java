package org.northpay_contractor_onboarding.repository;

import org.northpay_contractor_onboarding.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymenMethodRepository extends JpaRepository<PaymentMethod, UUID> {

}
