package com.epam.spsa.validation.validators;

import com.epam.spsa.model.ActivityTime;
import com.epam.spsa.validation.ActivityTimeValue;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class ActivityTimeValidator implements ConstraintValidator<ActivityTimeValue, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (name != null) {
            log.info("Validation of day time with value: {}", name);
            return ActivityTime.isActivityTime(name);
        }
        return true;
    }

}
