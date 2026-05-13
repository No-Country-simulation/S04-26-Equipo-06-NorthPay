package org.northpay_contractor_onboarding.controller;

import org.northpay_contractor_onboarding.dto.authentication.LoginDTO;
import org.northpay_contractor_onboarding.dto.jwt.TokenDTO;
import org.northpay_contractor_onboarding.security.authentication.AuthenticationService;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginInfo) {    
    return new ResponseEntity<TokenDTO>(authenticationService.login(loginInfo), HttpStatus.OK);
  }

  @PostMapping("/logout")
  @PreAuthorize("hasAnyRole('ROLE_OPERATOR')")
  public ResponseEntity<TokenDTO> logout() {
    return new ResponseEntity<TokenDTO>(authenticationService.logout(), HttpStatus.OK);
  }
}
