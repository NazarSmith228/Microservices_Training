package com.epam.slsa.service;

import com.epam.slsa.dao.impl.LocationTypeDaoImpl;
import com.epam.slsa.dto.locationType.LocationTypeDto;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.model.LocationType;
import com.epam.slsa.service.impl.LocationTypeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LocationTypeServiceTest {
    @InjectMocks
    LocationTypeServiceImpl locationTypeService;

    @Mock
    LocationTypeDaoImpl dao;

    @Mock
    ModelMapper modelMapper;

    private LocationType getLocationType(int id) {
        return LocationType.builder()
                .id(id)
                .name("Park")
                .build();
    }

    private LocationTypeDto getLocationTypeDto(int id) {
        return LocationTypeDto.builder()
                .id(id)
                .name("Park")
                .build();
    }

    @Test
    void getByIdTest() {
        int id = 1;

        when(dao.getById(id))
                .thenAnswer((Answer<LocationType>) invocationOnMock -> getLocationType(invocationOnMock.getArgument(0)));

        when(modelMapper.map(Mockito.any(LocationType.class), Mockito.eq(LocationTypeDto.class)))
                .thenAnswer((Answer<LocationTypeDto>) invocationOnMock -> {
                    LocationType location = invocationOnMock.getArgument(0);
                    return getLocationTypeDto(location.getId());
                });

        LocationTypeDto result = locationTypeService.getById(id);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Park", result.getName());
    }

    @Test
    void getAllTest() {
        List<LocationType> resultList = new ArrayList<>();
        resultList.add(getLocationType(1));

        when(dao.getAll()).thenReturn(resultList);

        when(modelMapper.map(Mockito.any(LocationType.class), Mockito.eq(LocationTypeDto.class)))
                .thenAnswer((Answer<LocationTypeDto>) invocationOnMock -> {
                    LocationType location = invocationOnMock.getArgument(0);
                    return getLocationTypeDto(location.getId());
                });

        Assertions.assertEquals(1, locationTypeService.getAll().size());
    }

    @Test
    void getByIdIncorrectTest() {
        int id = 1;

        when(dao.getById(id))
                .thenAnswer((Answer<LocationType>) invocationOnMock -> getLocationType(invocationOnMock.getArgument(0)));

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> locationTypeService.getById(666));
    }

}