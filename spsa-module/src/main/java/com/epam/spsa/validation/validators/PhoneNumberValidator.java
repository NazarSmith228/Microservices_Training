package com.epam.spsa.validation.validators;

import com.epam.spsa.dao.UserDao;
import com.epam.spsa.validation.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.NoResultException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
@Slf4j
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private final UserDao userDao;

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Checking if phone number is used: {}", phoneNumber);
        try {
            userDao.getByPhoneNumber(phoneNumber);
            return false;
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return true;
        }
    }

}
