package com.epam.spsa.service;

import com.epam.spsa.dao.SportTypeDao;
import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.SportType;
import com.epam.spsa.service.impl.SportTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class SportTypeServiceTest {

    @InjectMocks
    private SportTypeServiceImpl sportTypeService;

    @Mock
    private SportTypeDao sportTypeDao;

    @Mock
    private ModelMapper mapper;

    @Test
    public void getSportTypeByIdTest() {
        int id = 1;
        SportType sportType = getSportType(id);

        Mockito.when(sportTypeDao.getById(id)).thenReturn(sportType);
        Mockito.when(mapper.map(sportType, SportTypeDto.class)).thenAnswer(
                (Answer<SportTypeDto>) invocationOnMock -> {
                    SportType sportType1 = invocationOnMock.getArgument(0);
                    return getSportTypeDto(sportType1.getId());
                });

        SportTypeDto sportTypeDto = sportTypeService.getSportTypeById(id);

        assertEquals(sportType.getId(), sportTypeDto.getId());
        assertEquals(sportType.getName(), sportTypeDto.getName());
    }

    @Test
    public void getSportTypeByIdIncorrectTest() {
        int id = 1;
        SportType sportType = getSportType(id);

        Mockito.when(sportTypeDao.getById(id)).thenReturn(sportType);

        assertThrows(EntityNotFoundException.class, () -> {
            sportTypeService.getSportTypeById(666);
        });
    }

    @Test
    public void getAllSportTypesTest() {
        SportType sportType = getSportType(1);
        SportType[] sportTypes = {sportType};

        Mockito.when(sportTypeDao.getAll()).thenReturn(Arrays.asList(sportTypes));

        Mockito.when(mapper.map(Mockito.any(SportType.class), Mockito.eq(SportTypeDto.class))).thenAnswer(
                (Answer<SportTypeDto>) invocationOnMock -> {
                    SportType sportType1 = invocationOnMock.getArgument(0);
                    return getSportTypeDto(sportType1.getId());
                });

        assertTrue(sportTypeService.getAllSportTypes().size() > 0);

        SportTypeDto sportTypeDto = sportTypeService.getAllSportTypes().get(0);

        assertEquals(sportType.getId(), sportTypeDto.getId());
        assertEquals(sportType.getName(), sportTypeDto.getName());
    }

    private SportType getSportType(int id) {
        return SportType.builder()
                .id(id)
                .name("Running")
                .build();
    }

    private SportTypeDto getSportTypeDto(int id) {
        return SportTypeDto.builder()
                .id(id)
                .name("Running")
                .build();
    }

}