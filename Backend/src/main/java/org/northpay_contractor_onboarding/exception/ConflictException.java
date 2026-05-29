package org.northpay_contractor_onboarding.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends ApiError{

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
        
    }
    
}
