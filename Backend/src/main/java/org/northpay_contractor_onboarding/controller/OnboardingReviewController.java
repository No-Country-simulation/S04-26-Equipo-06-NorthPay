package org.northpay_contractor_onboarding.controller;

import java.util.UUID;

import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingReviewDTO;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingReviewRequest;
import org.northpay_contractor_onboarding.model.OnboardingReview;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.northpay_contractor_onboarding.service.OnboardingReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/onboarding")
@AllArgsConstructor
public class OnboardingReviewController {

    private final OnboardingReviewService onboardingReviewService;
 
    public ResponseEntity<OnboardingReviewDTO> createReview(
        @PathVariable UUID onboardingId ,
            @RequestBody OnboardingReviewRequest onboardingReviwRequest
        ) {

        OnboardingReviewDTO onboardingReview = onboardingReviewService.createReview(onboardingId , onboardingReviwRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(onboardingReview);

    }
    public ResponseEntity<OnboardingReviewDTO> getOnboardingReview(@PathVariable UUID onboardingId ){

      OnboardingReviewDTO onboardingReview = onboardingReviewService.getOnboardingReview(onboardingId );

        return ResponseEntity.ok(onboardingReview);
    }

}
