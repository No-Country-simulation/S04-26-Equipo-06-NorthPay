package org.northpay_contractor_onboarding.repository;

import java.util.UUID;

import org.northpay_contractor_onboarding.model.Onboarding;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OnboardingRepository extends JpaRepository<Onboarding, UUID> {

}
