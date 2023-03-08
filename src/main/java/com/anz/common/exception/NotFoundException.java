package com.anz.common.exception;

import lombok.Getter;

public class NotFoundException extends RuntimeException {
    @Getter
    private final String errorMessage;

    public NotFoundException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
