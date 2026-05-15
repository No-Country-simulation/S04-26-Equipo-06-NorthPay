package org.northpay_contractor_onboarding.controller;

import java.util.UUID;

import org.northpay_contractor_onboarding.dto.OnboardingDTO;

import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.service.IOnboardiIngService;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class OnboardingController {

    private IOnboardiIngService onboardingService;

    @PutMapping("/{id}/dataPersonal")
    public ResponseEntity<OnboardingDTO> saveDatePersonal(
            @Valid @RequestBody OnboardingDTO.RequestOnboarding requestOnboarding,
            @PathVariable UUID id) {

        OnboardingDTO onboarding = onboardingService.savePersonalData(id, requestOnboarding);

        return ResponseEntity.status(HttpStatus.CREATED).body(onboarding);

    }

    @PostMapping("/onboarding")
    public ResponseEntity<Onboarding> onboardingInvitation() {

        Onboarding onboarding = onboardingService.create();

        return ResponseEntity.status(HttpStatus.CREATED).body(onboarding);

    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<OnboardingDTO> completeProcess(@PathVariable UUID id) {

        OnboardingDTO response = onboardingService.finalizeOnboarding(id);

        return ResponseEntity.ok(response);
    }

}
