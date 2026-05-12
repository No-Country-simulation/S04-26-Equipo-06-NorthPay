package org.northpay_contractor_onboarding.dto;

public record InvitationTokenDTO(String id, String token, boolean used, String expiresAt, String onboardingId) {}
