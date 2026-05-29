package org.northpay_contractor_onboarding.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;

@Builder
public record ErrorResponse(
    int status,
    String message,
    LocalDateTime timestamp,
    Map<String, String> details,
    String error,
    StackTraceElement[] stackTrace
) {}
