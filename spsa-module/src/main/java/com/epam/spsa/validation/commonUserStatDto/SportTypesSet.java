package com.epam.spsa.validation.commonUserStatDto;

import com.epam.spsa.validation.validators.commonUserStatDto.SportTypesSetValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = SportTypesSetValidator.class)
public @interface SportTypesSet {

    String message() default "{user.stats.sportTypesSetError}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
