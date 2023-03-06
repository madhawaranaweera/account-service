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
@Size(min=3, max=3)
@Pattern(regexp="[A-Z]{3}")
@NotNull
@ReportAsSingleViolation
public @interface ValidCurrency {
    String message() default "Invalid currency";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
