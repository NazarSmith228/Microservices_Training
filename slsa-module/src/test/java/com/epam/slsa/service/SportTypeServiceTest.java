package com.epam.slsa.service;

import com.epam.slsa.dao.SportTypeDao;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.model.Coach;
import com.epam.slsa.model.SportType;
import com.epam.slsa.service.impl.SportTypeServiceImpl;
import org.assertj.core.util.Lists;
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
public class SportTypeServiceTest {

    @InjectMocks
    private SportTypeServiceImpl sportTypeService;

    @Mock
    private SportTypeDao sportTypeDao;

    @Mock
    private ModelMapper modelMapper;

    private SportType getSportType(int sportTypeId) {
        return SportType.builder()
                .id(sportTypeId)
                .name("Running")
                .coaches(Lists.newArrayList(
                        Coach.builder()
                                .id(1)
                                .build()))
                .build();
    }

    private SportTypeDto getSportTypeDto(int sportTypeId) {
        return SportTypeDto.builder()
                .id(sportTypeId)
                .name("Running")
                .build();
    }

    @Test
    public void getByIdTest() {
        int sportTypeId = 1;

        SportType sportType = getSportType(sportTypeId);

        when(sportTypeDao.getById(sportTypeId)).thenAnswer((Answer<SportType>) invocationOnMock ->
                getSportType(invocationOnMock.getArgument(0)));

        when(modelMapper.map(Mockito.any(SportType.class), Mockito.eq(SportTypeDto.class)))
                .thenReturn(getSportTypeDto(sportTypeId));

        SportTypeDto sportTypeDto = sportTypeService.getById(sportTypeId);
        Assertions.assertEquals((sportType.getName()), sportTypeDto.getName());
    }

    @Test
    public void getByIdIncorrectTest() {
        int sportTypeIncorrectId = 100;

        when(sportTypeDao.getById(sportTypeIncorrectId)).thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                sportTypeService.getById(sportTypeIncorrectId));
    }

    @Test
    public void getAllTest() {
        List<SportType> sportTypeList = new ArrayList<>();
        sportTypeList.add(getSportType(1));
        sportTypeList.add(getSportType(2));

        when(sportTypeDao.getAll()).thenReturn(sportTypeList);

        when(modelMapper.map(Mockito.any(SportType.class), Mockito.eq(SportTypeDto.class)))
                .thenReturn(getSportTypeDto(1));

        List<SportTypeDto> sportTypeDtoList = sportTypeService.getAll();

        Assertions.assertEquals(sportTypeList.size(), sportTypeDtoList.size());
    }

}
