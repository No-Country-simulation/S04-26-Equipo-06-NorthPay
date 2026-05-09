package org.northpay_contractor_onboarding.security.authentication;

import java.util.Collection;
import java.util.List;

import org.northpay_contractor_onboarding.enums.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

// Clase que implementa UserDetails de Spring security, que se usa internamente para la autenticación
@Data @AllArgsConstructor
public class AuthenticatedUserDetails implements UserDetails {
  private String email;
  private String password;
  private Roles role; // Vendría de un Enum

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    GrantedAuthority rolePermission = new SimpleGrantedAuthority("ROLE_" + this.getRole().toString());
    return List.of(rolePermission);
  }
}
