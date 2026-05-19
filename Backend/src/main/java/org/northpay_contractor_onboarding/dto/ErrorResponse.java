package org.northpay_contractor_onboarding.dto;

import java.time.LocalDateTime;



public record ErrorResponse(
    String message,
    int statusCode,
    LocalDateTime timestamp
) {}
