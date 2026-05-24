package org.northpay_contractor_onboarding.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.northpay_contractor_onboarding.enums.Roles;
import org.springframework.context.annotation.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, updatable = false)
    private String role;

    @Column(nullable = false, updatable = false)
    private String action;

    @Column(name = "resource_affected", nullable = false, updatable = false, length = 500)
    private String resourceAffected;

    @Column(nullable = false, updatable = false)
    private String result;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

}
