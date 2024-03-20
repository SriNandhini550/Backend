package com.dxc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserSignupException extends RuntimeException {
    public UserSignupException(String message) {
        super(message);
    }
}
