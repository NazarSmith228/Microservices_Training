package com.epam.spsa.validation;

import com.epam.spsa.validation.validators.LocalDateTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = LocalDateTypeValidator.class)
public @interface LocalDateType {

    String message() default "{dateFormat.incorrect}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
