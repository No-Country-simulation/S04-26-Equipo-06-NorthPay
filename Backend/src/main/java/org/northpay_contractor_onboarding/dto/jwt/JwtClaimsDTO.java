package org.northpay_contractor_onboarding.dto.jwt;

import org.northpay_contractor_onboarding.enums.JwtTypes;
import org.northpay_contractor_onboarding.enums.Roles;

public record JwtClaimsDTO(String name, Roles role, JwtTypes type) {}
