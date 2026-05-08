package org.northpay_contractor_onboarding.repository;

import org.northpay_contractor_onboarding.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContractRepository  extends JpaRepository<Contract, UUID> {

}
