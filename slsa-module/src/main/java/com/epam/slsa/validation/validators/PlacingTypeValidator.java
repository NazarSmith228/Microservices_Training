package com.epam.slsa.validation.validators;

import com.epam.slsa.model.Placing;
import com.epam.slsa.validation.PlacingType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PlacingTypeValidator implements ConstraintValidator<PlacingType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Placing.isValidPlacing(value);
    }

}

