package com.epam.slsa.validation.validators;

import com.epam.slsa.model.Day;
import com.epam.slsa.validation.DayType;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DayTypeValidator implements ConstraintValidator<DayType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isEmpty(value) || Day.isValidDay(value);
    }

}

