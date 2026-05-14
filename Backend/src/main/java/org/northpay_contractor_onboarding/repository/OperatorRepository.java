package org.northpay_contractor_onboarding.repository;

import java.util.Optional;
import java.util.UUID;

import org.northpay_contractor_onboarding.model.Operators;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperatorRepository extends JpaRepository<Operators, UUID> {
  Optional<Operators> findByEmail(String email);
}
