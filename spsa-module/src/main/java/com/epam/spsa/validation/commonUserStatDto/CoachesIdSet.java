package com.epam.spsa.validation.commonUserStatDto;

import com.epam.spsa.validation.validators.commonUserStatDto.CoachesIdSetValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = CoachesIdSetValidator.class)
public @interface CoachesIdSet {

    String message() default "{user.stats.coachesIdSetError}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
