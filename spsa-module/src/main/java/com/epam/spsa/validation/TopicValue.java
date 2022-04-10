package com.epam.spsa.validation;


import com.epam.spsa.validation.validators.TopicValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = TopicValidator.class)
public @interface TopicValue {

    String message() default "{blog.topic.notUnique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
