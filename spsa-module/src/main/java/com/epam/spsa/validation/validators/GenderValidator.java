package com.epam.spsa.validation.validators;

import com.epam.spsa.model.Gender;
import com.epam.spsa.validation.GenderType;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class GenderValidator implements ConstraintValidator<GenderType, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (name != null) {
            log.info("Validating Gender with value: {}", name);
            return Gender.isGender(name);
        }
        return true;
    }

}
