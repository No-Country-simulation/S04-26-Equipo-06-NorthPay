package org.northpay_contractor_onboarding.security.authentication;

import org.northpay_contractor_onboarding.enums.Roles;
import org.northpay_contractor_onboarding.exception.NotFoundException;
import org.northpay_contractor_onboarding.model.Operators;
import org.northpay_contractor_onboarding.repository.OperatorRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AuthenticationSystemProvider {
  private final OperatorRepository operatorRepository;

  @Bean
  public UserDetailsService userDetailsService() {
    return email -> {
      Operators operatorToAuthenticate = operatorRepository.findByEmail(email).orElseThrow(
        () -> new NotFoundException("User not found")
      );

      return new AuthenticatedUserDetails(operatorToAuthenticate.getEmail(), operatorToAuthenticate.getPassword(), Roles.OPERATOR);
    };
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
