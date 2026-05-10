package org.northpay_contractor_onboarding.security.jwt;

import java.io.IOException;

import org.northpay_contractor_onboarding.enums.Roles;
import org.northpay_contractor_onboarding.security.authentication.AuthenticatedUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return ;
    }

    String token = authHeader.substring(7);

    try {
      String tokenSubject_email = jwtService.getClaim(token, claims -> claims.getSubject());
      Roles tokenRole = Roles.OPERATOR;
      if (tokenSubject_email == null) {
        filterChain.doFilter(request, response);
        return;
      }

      String token_userName = jwtService.getClaim(token, claims -> claims.get("name", String.class));

      if (jwtService.isTokenExpired(token_userName)) {
        filterChain.doFilter(request, response);
        return;
      }

      Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
      if (existingAuth != null && existingAuth.getName().equals(tokenSubject_email)) {
        filterChain.doFilter(request, response);
        return;
      }

      AuthenticatedUserDetails newAuthData = new AuthenticatedUserDetails(tokenSubject_email, "", tokenRole);

      PreAuthenticatedAuthenticationToken newAuth = new PreAuthenticatedAuthenticationToken(
        newAuthData,
        null
      );
      newAuth.setAuthenticated(true);
      newAuth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(newAuth);
    } catch (Exception e) {
      // TODO: handle exceptions, separar por excepciones arrojadas del service y de las que no
      System.out.println(e.getMessage());
    }
  }

  
}
