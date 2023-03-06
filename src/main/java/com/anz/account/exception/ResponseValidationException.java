package com.anz.account.exception;

import lombok.Getter;

public class ResponseValidationException extends RuntimeException {
    @Getter
    private final String errorMessage;

    public ResponseValidationException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
