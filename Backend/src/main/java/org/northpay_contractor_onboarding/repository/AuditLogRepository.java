package org.northpay_contractor_onboarding.repository;

import java.util.UUID;

import org.northpay_contractor_onboarding.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog,UUID>{
    
}
