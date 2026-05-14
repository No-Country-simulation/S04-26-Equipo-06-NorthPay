package org.northpay_contractor_onboarding.controller;

import java.util.List;

import org.northpay_contractor_onboarding.dto.operatorDtos.OperatorDTO;
import org.northpay_contractor_onboarding.dto.operatorDtos.OperatorRegistrationDTO;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.service.implementations.OperatorsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/operator")
@Validated
@RequiredArgsConstructor
public class OperatorController {
  private final OperatorsService operatorsService;

  @GetMapping()
  @PreAuthorize("hasAnyRole('OPERATOR')")
  public ResponseEntity<List<OperatorDTO>> getAll() {
    return new ResponseEntity<>(operatorsService.getAll(), HttpStatus.OK);
  }
  
  @PreAuthorize("hasAnyRole('OPERATOR')")
  @GetMapping("/{email}")
  public ResponseEntity<OperatorDTO> getByEmail(@Email @PathVariable String email) {
    return new ResponseEntity<>(operatorsService.getByEmail(email), HttpStatus.OK);
  }
  
  @PostMapping("/register")
  public ResponseEntity<OperatorDTO> register(@Valid @RequestBody OperatorRegistrationDTO dto) {
    return new ResponseEntity<>(operatorsService.create(dto), HttpStatus.CREATED);
  }

  @PostMapping("/dev/onboarding")
  @Operation(description = "FOR TESTING ONLY")
  public Onboarding postMethodName() {
    return operatorsService.createOnboardingTEST();
  }
  

  // TODO: método para cambiar contraseña, cambiar mail, y borrar
}
