package org.northpay_contractor_onboarding.service;

import java.time.LocalDateTime;

import org.northpay_contractor_onboarding.enums.Roles;
import org.northpay_contractor_onboarding.model.AuditLog;
import org.northpay_contractor_onboarding.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(String actor, String action, String resource, String result) {
        AuditLog log = AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .role(actor)
                .action(action)
                .resourceAffected(resource)
                .result(result)
                .build();
        auditLogRepository.save(log);
    }

}
