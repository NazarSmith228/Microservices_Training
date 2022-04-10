package com.epam.spsa.validation;

import com.epam.spsa.validation.validators.MaturityValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MaturityValidator.class)
public @interface MaturityType {

    String message() default "{maturity.unsupported}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
