package com.epam.slsa.validation;

import com.epam.slsa.dto.coach.CoachDto;
import com.epam.slsa.dto.location.LocationDto;
import com.epam.slsa.dto.locationSchedule.LocationScheduleDto;
import com.epam.slsa.model.Day;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalTime;
import java.util.Set;

import static com.epam.slsa.builders.coach.CoachDtoBuilder.getCoachDto;
import static com.epam.slsa.builders.location.LocationDtoBuilder.getLocationDto;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ValidationTest {

    @Autowired
    private Validator validator;

    @Test
    public void invalidUserTest() {
        CoachDto coach = getCoachDto();
        coach.setRating(-1);
        Set<ConstraintViolation<CoachDto>> violations = validator.validate(coach);
        assertTrue(violations.size() > 0);
    }

    @Test
    public void validLocationScheduleTest() {
        LocationScheduleDto locationSchedule = LocationScheduleDto.builder().day(Day.MONDAY.getName()).startTime(LocalTime.of(10, 00).toString()).endTime(LocalTime.of(18, 00).toString()).build();
        Set<ConstraintViolation<LocationScheduleDto>> violations = validator.validate(locationSchedule);
        assertEquals(0, violations.size());
    }

    @Test
    public void invalidLocationScheduleTest() {
        LocationScheduleDto locationSchedule = LocationScheduleDto.builder().day("tuesdd").startTime("10:t0").endTime(LocalTime.of(18, 00).toString()).build();
        Set<ConstraintViolation<LocationScheduleDto>> violations = validator.validate(locationSchedule);
        assertEquals(2, violations.size());
    }

    @Test
    public void invalidLocationTest() {
        LocationDto locationDto = getLocationDto();
        Set<ConstraintViolation<LocationDto>> violations = validator.validate(locationDto);
        assertEquals(2, violations.size());
    }

}
