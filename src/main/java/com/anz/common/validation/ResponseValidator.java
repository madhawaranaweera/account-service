package com.anz.common.validation;

import com.anz.common.exception.ResponseValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ResponseValidator {
    private final Validator validator;

    public <R> R applyValidations(R object) {
        Set<ConstraintViolation<R>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            throw new ResponseValidationException(violations.toString());
        } else {
            return object;
        }
    }
}
