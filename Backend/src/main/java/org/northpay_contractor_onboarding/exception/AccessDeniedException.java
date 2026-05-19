package org.northpay_contractor_onboarding.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends ApiError {

    public AccessDeniedException(String message, HttpStatus status) {
        super(message, HttpStatus.FORBIDDEN);
       
    }
    
}
