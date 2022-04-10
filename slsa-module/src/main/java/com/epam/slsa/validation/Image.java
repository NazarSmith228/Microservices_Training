package com.epam.slsa.validation;

import com.epam.slsa.validation.validators.FileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = FileValidator.class)
public @interface Image {

    String message() default "{location.photo.badfile}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
