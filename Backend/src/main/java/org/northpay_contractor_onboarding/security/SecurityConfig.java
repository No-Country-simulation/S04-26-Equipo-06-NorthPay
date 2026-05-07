package org.northpay_contractor_onboarding.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity @EnableWebSecurity
public class SecurityConfig {
  
  @Bean
  SecurityFilterChain setFilterChain(HttpSecurity http) throws Exception {
    http.
      authorizeHttpRequests(req ->
        req.anyRequest().permitAll() // TODO: esto es temporal, definir las rutas según los roles
      )
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Debido a que se usará JWT
      )
      .authenticationProvider(null)
      .addFilterBefore(null, null);

    return http.build();
  }
}
