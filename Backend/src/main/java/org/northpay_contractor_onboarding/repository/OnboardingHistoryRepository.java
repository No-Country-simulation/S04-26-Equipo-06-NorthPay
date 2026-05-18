package org.northpay_contractor_onboarding.repository;

import java.util.UUID;

import org.northpay_contractor_onboarding.model.OnboardingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnboardingHistoryRepository extends JpaRepository<OnboardingHistory, UUID> {
 
}
