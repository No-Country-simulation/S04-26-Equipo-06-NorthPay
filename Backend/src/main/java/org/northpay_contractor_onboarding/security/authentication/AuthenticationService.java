package org.northpay_contractor_onboarding.security.authentication;

import org.northpay_contractor_onboarding.dto.authentication.LoginDTO;
import org.northpay_contractor_onboarding.dto.jwt.JwtClaimsDTO;
import org.northpay_contractor_onboarding.dto.jwt.TokenDTO;
import org.northpay_contractor_onboarding.enums.JwtTypes;
import org.northpay_contractor_onboarding.enums.Roles;
import org.northpay_contractor_onboarding.model.Operators;
import org.northpay_contractor_onboarding.repository.OperatorRepository;
import org.northpay_contractor_onboarding.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final OperatorRepository operatorRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Transactional(readOnly = true)
  public TokenDTO login(LoginDTO inputInfo) {
    String email = inputInfo.email();

    // Revisa que en el contexto de seguridad no haya ya alguna autenticación realizada
    Authentication currentAuthInContext = SecurityContextHolder.getContext().getAuthentication();
    if (currentAuthInContext.getPrincipal() instanceof AuthenticatedUserDetails)
      throw new RuntimeException("Ya hay una sesión de cuenta iniciada");

    Operators operatorToLog = operatorRepository.findByEmail(email).orElseThrow(
      () -> new RuntimeException("cuenta nombre de usuario")
    );

    String token = jwtService.generateToken(new JwtClaimsDTO(operatorToLog.getName(), Roles.OPERATOR, JwtTypes.operatorAuth), email);

    // En este paso es donde se guarda la autenticación en el contexto de seguridad, mediante al authenticationManager de Spring Security
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        new AuthenticatedUserDetails(operatorToLog.getEmail(), operatorToLog.getName(), inputInfo.password(), Roles.OPERATOR),
        inputInfo.password()
      )
    );

    return new TokenDTO(token, operatorToLog.getName());
  }

  public TokenDTO logout() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !(auth.getPrincipal() instanceof AuthenticatedUserDetails)) {
      throw new RuntimeException("No hay cuenta en sesión para cerrar");
    }

    SecurityContextHolder.clearContext();

    String logoutToken = jwtService.generateToken(new JwtClaimsDTO(null, null, null), null);
    return new TokenDTO(logoutToken, null);
  }
}
