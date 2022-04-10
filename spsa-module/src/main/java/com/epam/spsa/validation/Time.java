package com.epam.spsa.validation;

import com.epam.spsa.validation.validators.TimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = TimeValidator.class)
public @interface Time {

    String message() default "{timeFormat.incorrect}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
