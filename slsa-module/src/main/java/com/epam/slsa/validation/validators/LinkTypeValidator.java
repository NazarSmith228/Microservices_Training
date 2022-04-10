package com.epam.slsa.validation.validators;


import com.epam.slsa.validation.LinkType;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class LinkTypeValidator implements ConstraintValidator<LinkType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validating Gender with value: {}", value);
        return com.epam.slsa.model.LinkType.isValidLinkType(value);
    }

}

