package com.epam.slsa.validation.validators;

import com.epam.slsa.validation.LocalTimeType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class LocalTimeTypeValidator implements ConstraintValidator<LocalTimeType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            LocalTime.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
