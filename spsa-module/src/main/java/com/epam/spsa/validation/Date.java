package com.epam.spsa.validation;

import com.epam.spsa.validation.validators.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = DateValidator.class)
public @interface Date {

    String message() default "{dateFormat.incorrect}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
