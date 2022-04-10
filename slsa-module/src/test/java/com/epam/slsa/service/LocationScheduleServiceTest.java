package com.epam.slsa.service;

import com.epam.slsa.dao.LocationDao;
import com.epam.slsa.dao.LocationScheduleDao;
import com.epam.slsa.dto.locationSchedule.LocationScheduleDto;
import com.epam.slsa.dto.locationSchedule.MainLocationScheduleDto;
import com.epam.slsa.model.Day;
import com.epam.slsa.model.Location;
import com.epam.slsa.model.LocationSchedule;
import com.epam.slsa.service.impl.LocationScheduleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(SpringExtension.class)
public class LocationScheduleServiceTest {

    @InjectMocks
    private LocationScheduleServiceImpl locationScheduleService;

    @Mock
    private LocationDao locationDao;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private LocationScheduleDao locationScheduleDao;

    private LocationSchedule getLocationSchedule() {
        return LocationSchedule.builder()
                .id(1)
                .day(Day.MONDAY)
                .build();
    }

    private LocationScheduleDto getLocationScheduleDto() {
        return LocationScheduleDto.builder()
                .day("MONDAY")
                .build();
    }

    private Location getLocation(int id) {
        return Location.builder()
                .id(id)
                .name("First step")
                .build();
    }

    @Test
    public void saveTest() {
        int locationId = 1;
        Set<LocationScheduleDto> scheduleDtos = new HashSet<>();
        scheduleDtos.add(getLocationScheduleDto());

        Mockito.when(locationScheduleDao.getAllByLocationId(locationId))
                .thenAnswer((Answer<List<LocationSchedule>>) invocationOnMock -> {
                    LocationSchedule locationSchedule = getLocationSchedule();
                    locationSchedule.setId(2);
                    locationSchedule.setDay(Day.TUESDAY);

                    List<LocationSchedule> list = new ArrayList<>();
                    list.add(locationSchedule);
                    return list;
                });

        Mockito.when(modelMapper.map(Mockito.any(LocationScheduleDto.class), Mockito.eq(LocationSchedule.class)))
                .thenAnswer((Answer<LocationSchedule>) invocationOnMock -> {
                    LocationScheduleDto locationScheduleDto = invocationOnMock.getArgument(0);
                    return LocationSchedule.builder()
                            .day(Day.fromName(locationScheduleDto.getDay()))
                            .build();
                });

        Mockito.when(modelMapper.map(Mockito.any(LocationSchedule.class), Mockito.eq(LocationScheduleDto.class)))
                .thenAnswer((Answer<LocationScheduleDto>) invocationOnMock -> {
                    LocationSchedule locationSchedule = invocationOnMock.getArgument(0);
                    return LocationScheduleDto.builder()
                            .day(locationSchedule.getDay().getName())
                            .build();
                });

        Mockito.when(locationDao.getById(locationId))
                .thenAnswer((Answer<Location>) invocationOnMock -> getLocation(invocationOnMock.getArgument(0)));

        Mockito.when(locationScheduleDao.save(Mockito.any(LocationSchedule.class)))
                .thenAnswer((Answer<LocationSchedule>) invocationOnMock -> invocationOnMock.getArgument(0));

        Set<MainLocationScheduleDto> mainLocationScheduleDtos =
                locationScheduleService.save(scheduleDtos, locationId);

        Assertions.assertTrue(mainLocationScheduleDtos.size() > 0);
    }

    @Test
    public void updateTest() {
        int locationId = 1;
        LocationScheduleDto updated = getLocationScheduleDto();
        updated.setEndTime(LocalTime.of(9, 0).toString());
        updated.setStartTime(LocalTime.of(18, 0).toString());

        Set<LocationScheduleDto> editedLocationScheduleDto = new HashSet<>();
        editedLocationScheduleDto.add(updated);

        Mockito.when(locationScheduleDao.getAllByLocationId(locationId))
                .thenAnswer((Answer<List<LocationSchedule>>) invocationOnMock -> {
                    List<LocationSchedule> list = new ArrayList<>();
                    list.add(getLocationSchedule());
                    return list;
                });

        Mockito.when(modelMapper.map(Mockito.any(LocationScheduleDto.class), Mockito.eq(LocationSchedule.class)))
                .thenAnswer((Answer<LocationSchedule>) invocationOnMock -> {
                    LocationScheduleDto locationScheduleDto = invocationOnMock.getArgument(0);
                    LocationSchedule locationSchedule = invocationOnMock.getArgument(1);

                    locationSchedule.setStartTime(LocalTime.parse(locationScheduleDto.getStartTime()));
                    locationSchedule.setEndTime(LocalTime.parse(locationScheduleDto.getEndTime()));
                    return locationSchedule;
                });

        Mockito.when(locationScheduleDao.save(Mockito.any(LocationSchedule.class)))
                .thenAnswer((Answer<LocationSchedule>) invocationOnMock -> invocationOnMock.getArgument(0));

        Set<LocationScheduleDto> locationScheduleDtos
                = locationScheduleService.update(editedLocationScheduleDto, locationId);

        Assertions.assertEquals(editedLocationScheduleDto, locationScheduleDtos);
    }

    @Test
    public void getAllByLocationIdTest() {
        int locationId = 1;

        Mockito.when(locationScheduleDao.getAllByLocationId(locationId))
                .thenAnswer((Answer<List<LocationSchedule>>) invocationOnMock -> {
                    ArrayList<LocationSchedule> arrayList = new ArrayList<>();
                    arrayList.add(getLocationSchedule());
                    return arrayList;
                });

        Mockito.when(locationDao.getById(anyInt())).thenReturn(getLocation(1));

        Mockito.when(modelMapper.map(Mockito.any(LocationSchedule.class), Mockito.eq(LocationScheduleDto.class)))
                .thenReturn(getLocationScheduleDto());

        Set<MainLocationScheduleDto> mainLocationScheduleDtos
                = locationScheduleService.getAllByLocationId(locationId);

        Assertions.assertTrue(mainLocationScheduleDtos.size() > 0);
    }
}