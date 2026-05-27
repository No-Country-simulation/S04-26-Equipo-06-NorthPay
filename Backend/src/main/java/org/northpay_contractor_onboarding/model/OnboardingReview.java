package org.northpay_contractor_onboarding.model;

import java.util.UUID;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OnboardingReview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Enumerated(EnumType.STRING)
    OnboardingStatus onboardingStatus;
    String reason;
    @ManyToOne
    @JoinColumn(name = "onboarding_id")
    Onboarding onboarding;

}
