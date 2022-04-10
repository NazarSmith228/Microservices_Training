package com.epam.slsa.mapper;

import com.epam.slsa.builders.coach.CoachDtoBuilder;
import com.epam.slsa.dto.coach.CoachDto;
import com.epam.slsa.model.Coach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static com.epam.slsa.builders.coach.CoachDtoBuilder.getCoachDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CoachDtoMapperTest {
    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void coachMapperTest() {
        CoachDto coachDto = getCoachDto();
        Coach coach = modelMapper.map(coachDto, Coach.class);
        Coach newCoach = CoachDtoBuilder.getCoach();
        newCoach.setId(0);
        newCoach.setComments(new ArrayList<>());

        assertEquals(coach.toString(), newCoach.toString());
    }

}
