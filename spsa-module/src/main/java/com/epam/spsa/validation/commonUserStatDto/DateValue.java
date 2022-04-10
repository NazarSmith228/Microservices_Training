package com.epam.spsa.validation.commonUserStatDto;

import com.epam.spsa.validation.validators.commonUserStatDto.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = DateValidator.class)
public @interface DateValue {
    
    String message() default "{user.stats.intervalDateError}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
