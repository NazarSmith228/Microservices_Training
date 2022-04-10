package com.epam.spsa.validation.validators.commonUserStatDto;

import com.epam.spsa.validation.commonUserStatDto.DateValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;


@Slf4j
@Component
public class DateValidator implements ConstraintValidator<DateValue, LocalDate> {

    @Override
    public boolean isValid(LocalDate s, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validation date {}", s);
        if (s == null) {
            return false;
        }
        LocalDate currentDate = LocalDate.now();
        return s.isBefore(currentDate) || s.equals(currentDate);
    }

}
