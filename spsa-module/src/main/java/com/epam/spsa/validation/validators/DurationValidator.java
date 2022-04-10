package com.epam.spsa.validation.validators;

import com.epam.spsa.error.exception.WrongDurationPatternException;
import com.epam.spsa.validation.DurationValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.format.DateTimeParseException;

@Slf4j
@Component
public class DurationValidator implements ConstraintValidator<DurationValue, String> {

    @Override
    public boolean isValid(String duration, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validating duration of statistic");
        try {
            return Duration.parse(duration).compareTo(Duration.ofDays(1)) <= 0;
        } catch (DateTimeParseException e) {
            throw new WrongDurationPatternException("Wrong duration pattern. Example: PT5H15M59S");
        }
    }

}
