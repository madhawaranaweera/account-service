package com.anz.account.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 9, max = 9)
@Pattern(regexp = "^[0-9]+$")
@NotNull(message = "Account number cannot be null")
@ReportAsSingleViolation
public @interface ValidAccountNumber {
    String message() default "Invalid account number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
