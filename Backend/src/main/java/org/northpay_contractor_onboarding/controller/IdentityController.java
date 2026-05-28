package org.northpay_contractor_onboarding.controller;



import java.util.UUID;

import org.northpay_contractor_onboarding.dto.IdentityValidationResponseDTO;
import org.northpay_contractor_onboarding.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/identity")
public class IdentityController {

    @Autowired
    private IdentityService identityService;

    
    @PostMapping("/verify/{onboardingId}")
    public ResponseEntity<IdentityValidationResponseDTO> verify(
            @PathVariable UUID onboardingId,
            @RequestParam("file") MultipartFile file) { 
        
        var response = identityService.validateIdentity(onboardingId, file);

        return ResponseEntity.ok(response);
    }
}
