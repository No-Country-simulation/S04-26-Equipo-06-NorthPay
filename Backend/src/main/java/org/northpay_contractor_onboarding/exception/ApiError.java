package org.northpay_contractor_onboarding.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ApiError extends RuntimeException {

    private final HttpStatus status;

  
    public ApiError(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

 
}
