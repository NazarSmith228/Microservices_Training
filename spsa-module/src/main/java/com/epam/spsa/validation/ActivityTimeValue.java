package com.epam.spsa.validation;

import com.epam.spsa.validation.validators.ActivityTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
@Documented
@Constraint(validatedBy = ActivityTimeValidator.class)
public @interface ActivityTimeValue {

    String message() default "{day.time.unsupported}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
