package com.epam.slsa.validation;

import com.epam.slsa.validation.validators.SportTypeSubsetValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = SportTypeSubsetValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface SportTypeSubset {

    String message() default "{sportType.incorrect}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
