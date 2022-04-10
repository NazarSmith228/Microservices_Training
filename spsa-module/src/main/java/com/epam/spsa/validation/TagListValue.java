package com.epam.spsa.validation;

import com.epam.spsa.validation.validators.TagListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = TagListValidator.class)
public @interface TagListValue {

    String message() default "{tag.unsupported}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
