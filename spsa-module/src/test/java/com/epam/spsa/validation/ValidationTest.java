
package com.epam.spsa.validation;

import com.epam.spsa.controller.builder.CriteriaDtoBuilder;
import com.epam.spsa.controller.builder.SportTypeDtoBuilder;
import com.epam.spsa.controller.builder.UserBuilder;
import com.epam.spsa.dto.criteria.CriteriaDto;
import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.dto.user.UserDto;
import com.epam.spsa.imageBuilder.FileBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ValidationTest {

    @Autowired
    private Validator validator;

    @Test
    public void validUserTest() {
        UserDto user = UserBuilder.getUserDto();
        SportTypeDto sportType = SportTypeDtoBuilder.getSportTypeDto();
        sportType.setId(1);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }

    @Test
    public void invalidUserTest() {
        UserDto user = UserBuilder.getUserDto();
        String invalidPhoneNumber = "-22231";
        String invalidEmail = "not an email";
        user.setPhoneNumber(invalidPhoneNumber);
        user.setEmail(invalidEmail);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);
        assertEquals(violations.size(), 2);
    }

    @Test
    public void criteriaValidationTest() {
        CriteriaDto criteria = CriteriaDtoBuilder.getCriteriaDto();
        criteria.getSportType().setId(1);
        Set<ConstraintViolation<CriteriaDto>> violations = validator.validate(criteria);
        assertEquals(0, violations.size());
    }

    @Test
    public void fileTest() throws IOException {
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());
        Set<ConstraintViolation<MockMultipartFile>> violations = validator.validate(multipartFile);
        assertEquals(0, violations.size());
    }

}