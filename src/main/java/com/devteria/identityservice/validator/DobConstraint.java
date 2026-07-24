package com.devteria.identityservice.validator;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
    String message() default "Invalid date of birth. User must be at least 18 years old.";

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
