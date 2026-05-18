package org.northpay_contractor_onboarding.repository;

import java.util.UUID;

import org.northpay_contractor_onboarding.model.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractorRepository extends JpaRepository<Contractor, UUID> {

}
