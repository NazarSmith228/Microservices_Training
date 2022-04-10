package com.epam.spsa.validation;

import com.epam.spsa.validation.validators.RoleTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = RoleTypeValidator.class)
public @interface RoleType {

    String message() default "{roleType.unsupported}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
