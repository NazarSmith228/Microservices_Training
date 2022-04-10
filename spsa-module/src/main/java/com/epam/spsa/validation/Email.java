package com.epam.spsa.validation;

import com.epam.spsa.validation.validators.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = EmailValidator.class)
public @interface Email {

    String message() default "{user.email.notunique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
