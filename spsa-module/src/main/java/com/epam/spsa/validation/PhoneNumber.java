package com.epam.spsa.validation;

import com.epam.spsa.validation.validators.PhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {

    String message() default "{user.phoneNumber.notunique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
