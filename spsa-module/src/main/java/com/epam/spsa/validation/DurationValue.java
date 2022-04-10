package com.epam.spsa.validation;

import com.epam.spsa.validation.validators.DurationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = DurationValidator.class)
public @interface DurationValue {

    String message() default "{user.stats.wrongDuration}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
