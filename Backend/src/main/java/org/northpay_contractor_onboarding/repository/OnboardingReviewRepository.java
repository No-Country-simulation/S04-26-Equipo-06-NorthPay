package org.northpay_contractor_onboarding.repository;

import java.util.UUID;

import org.northpay_contractor_onboarding.model.OnboardingReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnboardingReviewRepository extends JpaRepository<OnboardingReview,UUID>  {
    
}
