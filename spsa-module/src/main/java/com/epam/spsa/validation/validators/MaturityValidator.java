package com.epam.spsa.validation.validators;

import com.epam.spsa.model.Maturity;
import com.epam.spsa.validation.MaturityType;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class MaturityValidator implements ConstraintValidator<MaturityType, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (name != null) {
            log.info("Validating Maturity with value: {}", name);
            return Maturity.isMaturity(name);
        }
        return true;
    }

}

