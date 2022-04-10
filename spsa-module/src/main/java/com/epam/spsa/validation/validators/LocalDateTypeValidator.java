package com.epam.spsa.validation.validators;

import com.epam.spsa.validation.LocalDateType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

public class LocalDateTypeValidator implements ConstraintValidator<LocalDateType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            LocalDate dateOfBirth = LocalDate.parse(value);
            return validate(dateOfBirth, context);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean validate(LocalDate dateOfBirth, ConstraintValidatorContext context) {
        LocalDate now = LocalDate.now();
        int age = now.minus(Period.ofYears(dateOfBirth.getYear())).getYear();
        if (age < 12) {
            context.disableDefaultConstraintViolation();
            ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder =
                    context.buildConstraintViolationWithTemplate("{user.age.minimum}");
            constraintViolationBuilder.addConstraintViolation();
            return false;
        } else if (age > 100) {
            context.disableDefaultConstraintViolation();
            ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder =
                    context.buildConstraintViolationWithTemplate("{user.age.maximum}");
            constraintViolationBuilder.addConstraintViolation();
            return false;
        }
        return true;
    }

}
