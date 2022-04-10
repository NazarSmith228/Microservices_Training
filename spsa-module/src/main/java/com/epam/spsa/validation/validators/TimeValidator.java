package com.epam.spsa.validation.validators;

import com.epam.spsa.validation.Time;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimeValidator implements ConstraintValidator<Time, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        try {
            java.sql.Time time = java.sql.Time.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
