package com.epam.spsa.validation;

import com.epam.spsa.validation.validators.TagValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = TagValidator.class)
public @interface TagValue {

    String message() default "{tag.unsupported}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
