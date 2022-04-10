package com.epam.spsa.validation.commonUserStatDto;


import com.epam.spsa.validation.validators.commonUserStatDto.LocationIdSetValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = LocationIdSetValidator.class)
public @interface LocationIdSet {

    String message() default "{user.stats.locationIdSetError}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
