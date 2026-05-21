package org.northpay_contractor_onboarding.controller;


import java.util.UUID;

import org.northpay_contractor_onboarding.dto.onboardingDtos.DataPersonalDTO;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingApproveRequest;
import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingDTO;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.service.IOnboardiIngService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/onboarding")
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
    @PatchMapping("/{id}")
    public ResponseEntity<OnboardingDTO> update(@PathVariable UUID id , @RequestBody Onboarding responseOnboardig){
              
        var onboardingDTO = onboardingService.update(id , responseOnboardig);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(onboardingDTO);
    } 

    @PostMapping("")
    public ResponseEntity<Onboarding> createOnboarding() {

        Onboarding onboarding = onboardingService.create();

        return ResponseEntity.status(HttpStatus.CREATED).body(onboarding);

    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<OnboardingDTO> completeProcess(@PathVariable UUID id) {

        OnboardingDTO response = onboardingService.finalizeOnboarding(id);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<OnboardingDTO> getOnboarding(@PathVariable UUID id){
          
        var onboarding = onboardingService.getOnboarding(id);
        
        return ResponseEntity.ok(onboarding);
    }

    @GetMapping("")
    public ResponseEntity<Page<OnboardingDTO>> getAllOnboarding(
        @PageableDefault(page = 0, size = 5) Pageable pageable)
        {
        
        Page<OnboardingDTO> onboarding = onboardingService.getAll(pageable);
        
        return ResponseEntity.ok(onboarding);

    }
    @GetMapping("{id}/dataPersonal")
    public ResponseEntity<DataPersonalDTO> dataPersonal(@PathVariable UUID id){

        var onboardingDataPersonal = onboardingService.dataPersonal(id);

        return ResponseEntity.ok(onboardingDataPersonal);
        
    }
    

}
