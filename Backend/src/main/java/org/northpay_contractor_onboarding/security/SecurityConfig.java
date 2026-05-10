package org.northpay_contractor_onboarding.security;

import org.northpay_contractor_onboarding.security.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity @EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtRequestFilter jwtRequestFilter;
  private final AuthenticationProvider authProvider;
  
  @Bean
  SecurityFilterChain setFilterChain(HttpSecurity http) throws Exception {
    http.
      authorizeHttpRequests(req ->
        req.anyRequest().permitAll() // TODO: esto es temporal, definir las rutas según los roles
      )
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Debido a que se usará JWT
      )
      .authenticationProvider(authProvider)
      .addFilterBefore(jwtRequestFilter, JwtRequestFilter.class)
      .cors(Customizer.withDefaults());

    return http.build();
  }
}
