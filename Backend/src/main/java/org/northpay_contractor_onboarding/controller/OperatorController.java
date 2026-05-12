package org.northpay_contractor_onboarding.controller;

import java.util.List;

import org.northpay_contractor_onboarding.dto.operatorDtos.OperatorDTO;
import org.northpay_contractor_onboarding.dto.operatorDtos.OperatorRegistrationDTO;
import org.northpay_contractor_onboarding.service.implementations.OperatorsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/operator")
@Validated
@RequiredArgsConstructor
public class OperatorController {
  private final OperatorsService operatorsService;

  @GetMapping()
  public ResponseEntity<List<OperatorDTO>> getAll() {
    return new ResponseEntity<>(operatorsService.getAll(), HttpStatus.OK);
  }
  
  @GetMapping("/{email}")
  public ResponseEntity<OperatorDTO> getByEmail(@Email @PathVariable String email) {
    return new ResponseEntity<>(operatorsService.getByEmail(email), HttpStatus.OK);
  }
  
  @PostMapping("/register")
  public ResponseEntity<OperatorDTO> register(@Valid @RequestBody OperatorRegistrationDTO dto) {
    return new ResponseEntity<>(operatorsService.create(dto), HttpStatus.CREATED);
  }

  // TODO: método para cambiar contraseña, cambiar mail, y borrar
}
