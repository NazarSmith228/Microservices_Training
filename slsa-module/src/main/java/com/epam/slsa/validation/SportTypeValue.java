package com.epam.slsa.validation;

import com.epam.slsa.validation.validators.SportTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = SportTypeValidator.class)
public @interface SportTypeValue {

    String message() default "{sportType.unsupported}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
