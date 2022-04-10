package com.epam.slsa.validation.validators;

import com.epam.slsa.model.Gender;
import com.epam.slsa.validation.GenderType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GenderTypeValidator implements ConstraintValidator<GenderType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Gender.isValidGender(value);
    }

}
