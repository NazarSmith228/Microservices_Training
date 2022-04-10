package com.epam.spsa.validation;

import com.epam.spsa.validation.validators.FileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = FileValidator.class)
public @interface Image {

    String message() default "{user.photo.badfile}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
