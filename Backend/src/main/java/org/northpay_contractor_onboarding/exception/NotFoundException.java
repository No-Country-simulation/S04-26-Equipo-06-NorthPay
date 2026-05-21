package org.northpay_contractor_onboarding.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiError {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
        
    }
   
}
