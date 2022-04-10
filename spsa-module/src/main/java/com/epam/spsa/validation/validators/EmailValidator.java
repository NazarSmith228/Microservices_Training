package com.epam.spsa.validation.validators;

import com.epam.spsa.dao.UserDao;
import com.epam.spsa.validation.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.NoResultException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
@Slf4j
public class EmailValidator implements ConstraintValidator<Email, String> {

    private final UserDao userDao;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Checking if email is used: {}", email);
        try {
            userDao.getByEmail(email);
            return false;
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return true;
        }
    }

}
