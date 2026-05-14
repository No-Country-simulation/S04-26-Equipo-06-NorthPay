package org.northpay_contractor_onboarding.enums;

/**
 * El prefijo `ROLE_` se inserta automáticamente cuando Spring inicializa el UserDetails, 
 * mediante {@link AuthenticatedUserDetails} en /security/authentication
 */
public enum Roles {
  OPERATOR,
  CONTRACTOR,
  ADMIN // A implementar si dan los tiempos
}
