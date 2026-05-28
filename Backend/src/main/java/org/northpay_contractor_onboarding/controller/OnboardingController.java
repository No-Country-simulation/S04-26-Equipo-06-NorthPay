package org.northpay_contractor_onboarding.controller;

import java.util.UUID;

import org.northpay_contractor_onboarding.dto.onboardingDtos.DataPersonalDTO;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingApproveRequest;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingChangeRequested;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingCompleteDTO;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingDTO;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingSummaryDTO;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.security.authentication.AuthenticatedUserDetails;
import org.northpay_contractor_onboarding.service.IOnboardiIngService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/onboarding")
@AllArgsConstructor
public class OnboardingController {

    private IOnboardiIngService onboardingService;

    @PutMapping("/{id}/dataPersonal")
    // @PreAuthorize("@authorizationService.contractorCanChangeOnboarding(#id)")
    public ResponseEntity<OnboardingDTO> saveDatePersonal(
            @Valid @RequestBody OnboardingDTO.RequestOnboarding requestOnboarding,
            @PathVariable UUID id) {

        OnboardingDTO onboarding = onboardingService.savePersonalData(id, requestOnboarding);

        return ResponseEntity.status(HttpStatus.CREATED).body(onboarding);

    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<OnboardingDTO> approve(@PathVariable UUID id,
            @RequestBody OnboardingApproveRequest requestOnboardig) {

        var onboardingDTO = onboardingService.approve(id, requestOnboardig);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(onboardingDTO);
    }

    @PatchMapping("/{id}/changeRequested")
    public ResponseEntity<OnboardingDTO> changeRequested(@PathVariable UUID id,
            @RequestBody OnboardingChangeRequested requestOnboardig) {

        var onboardingDTO = onboardingService.changeRequested(id, requestOnboardig);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(onboardingDTO);
    }

    @PostMapping("/createOnboarding")
    @PreAuthorize("hasAnyRole('OPERATOR')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Onboarding> createOnboarding(
        @RequestParam String destinedContractorEmail,
        @AuthenticationPrincipal AuthenticatedUserDetails loggedOperator
    ) {

        Onboarding onboarding = onboardingService.create(destinedContractorEmail, loggedOperator);

        return ResponseEntity.status(HttpStatus.CREATED).body(onboarding);

    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<OnboardingDTO> completeProcess(@PathVariable UUID id) {

        OnboardingDTO response = onboardingService.finalizeOnboarding(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<OnboardingDTO> getOnboarding(@PathVariable UUID id) {

        var onboarding = onboardingService.getOnboarding(id);

        return ResponseEntity.ok(onboarding);
    }

    @GetMapping("{id}/dataPersonal")
    public ResponseEntity<DataPersonalDTO> dataPersonal(@PathVariable UUID id) {

        var onboardingDataPersonal = onboardingService.dataPersonal(id);

        return ResponseEntity.ok(onboardingDataPersonal);

    }

    @GetMapping("admin/{id}")
    public ResponseEntity<OnboardingCompleteDTO> getOnboardingAdmin(@PathVariable UUID id) {

        var onboarding = onboardingService.getOnboardinAmin(id);

        return ResponseEntity.ok(onboarding);
    }

    @GetMapping("admin/list")
    public ResponseEntity<Page<OnboardingSummaryDTO>> getAllOnboarding(
            @PageableDefault(page = 0, size = 5) Pageable pageable) {

        Page<OnboardingSummaryDTO> onboarding = onboardingService.getAll(pageable);

        return ResponseEntity.ok(onboarding);

    }

}
