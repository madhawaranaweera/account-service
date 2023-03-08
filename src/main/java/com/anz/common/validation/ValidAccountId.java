package com.anz.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^[0-9]*$")
@NotNull(message = "Account id cannot be null")
@ReportAsSingleViolation
public @interface ValidAccountId {
    String message() default "Invalid account id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
