package com.anz.common.exception;

import com.anz.common.api.ApiError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiError> handleRequestValidationException(final Exception exception, final HttpServletRequest request) {
        log.error("Account service encountered a request validation error. \n error message = {}", exception.getMessage());
        return new ResponseEntity<>(ApiError.builder()
                .errorId("API-400")
                .message(exception.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class, ResponseValidationException.class})
    public ResponseEntity<ApiError> handleException(final Exception exception, final HttpServletRequest request) {
        log.error("Account service encountered an unexpected internal error. \n error message = {}", exception.getMessage());
        return new ResponseEntity<>(ApiError.builder()
                .errorId("API-500")
                .message(exception.getMessage())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiError> handleNotFoundException(final Exception exception, final HttpServletRequest request) {
        log.error("Account service is unable find requested resource. \n error message = {}", exception.getMessage());
        return new ResponseEntity<>(ApiError.builder()
                .errorId("API-404")
                .message(exception.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build(),
                HttpStatus.NOT_FOUND);
    }

}
