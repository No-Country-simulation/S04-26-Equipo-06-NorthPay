package org.northpay_contractor_onboarding.service;

import java.util.UUID;

import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingReviewDTO;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingReviewRequest;
import org.northpay_contractor_onboarding.exception.NotFoundException;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.model.OnboardingReview;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.northpay_contractor_onboarding.repository.OnboardingReviewRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OnboardingReviewService {

    private final OnboardingReviewRepository onboardingReviewRepository;
    private final OnboardingRepository onboardingRepository;

    public OnboardingReviewDTO createReview(UUID onboardingId, OnboardingReviewRequest onboardingReviewRequest) {

        Onboarding onboarding = onboardingRepository.findById(onboardingId)
                .orElseThrow(() -> new NotFoundException("Onboarding not found with ID: " + onboardingId));


        OnboardingReview onboardingReview = new OnboardingReview();
        onboardingReview.setOnboardingStatus(onboardingReviewRequest.getOnboardingStatus());
        onboardingReview.setReason(onboardingReviewRequest.getReason());
        onboardingReview.setOnboarding(onboarding);
        
        var onboardingReviewDb = onboardingReviewRepository.save(onboardingReview);

        OnboardingReviewDTO onboardingReviewDTO = new OnboardingReviewDTO();
        onboardingReviewDTO.setOnboardingStatus(onboardingReviewDb.getOnboardingStatus());
        onboardingReviewDTO.setId(onboardingReviewDb.getId());
        onboardingReviewDTO.setReason(onboardingReviewDb.getReason());
        onboardingReviewDTO.setOnboardingId(onboardingReviewDb.getOnboarding().getId());
        

        return onboardingReviewDTO;
    }

    public OnboardingReviewDTO getOnboardingReview(UUID onboardingId) {
       
        OnboardingReview onboardingReviewDb = onboardingReviewRepository.findById(onboardingId).orElseThrow(
            () -> new NotFoundException("onboardingReview not found ")
        );


        OnboardingReviewDTO onboardingReviewDTO = new OnboardingReviewDTO();
        onboardingReviewDTO.setOnboardingStatus(onboardingReviewDb.getOnboardingStatus());
        onboardingReviewDTO.setId(onboardingReviewDb.getId());
        onboardingReviewDTO.setReason(onboardingReviewDb.getReason());
        onboardingReviewDTO.setOnboardingId(onboardingReviewDb.getOnboarding().getId());
        
        return onboardingReviewDTO;

    }

}
