package org.northpay_contractor_onboarding.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OnboardingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboarding_id")
    private Onboarding onboarding;
    @Column(name = "old_status")
    @Enumerated(EnumType.STRING)
    private OnboardingStatus oldStatus;
    @Column(name = "new_status")
    @Enumerated(EnumType.STRING)
    private OnboardingStatus newStatus;
    @Column(name = "changed_by")
    private String changedBy;
    @Column(name = "reason")
    private String reason;
    @Column(name = "changed_at")
    private LocalDateTime changedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id")
    private Operators Operators;

}
