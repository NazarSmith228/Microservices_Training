package com.epam.spsa.validation;

import com.epam.spsa.validation.validators.GenderValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = GenderValidator.class)
public @interface GenderType {

    String message() default "{gender.unsupported}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
