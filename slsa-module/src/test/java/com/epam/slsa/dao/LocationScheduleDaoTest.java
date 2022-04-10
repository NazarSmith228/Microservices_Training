package com.epam.slsa.dao;

import com.epam.slsa.builders.locationType.LocationTypeBuilder;
import com.epam.slsa.model.Day;
import com.epam.slsa.model.Location;
import com.epam.slsa.model.LocationSchedule;
import com.epam.slsa.model.LocationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.slsa.builders.location.LocationDtoBuilder.getLocation;
import static com.epam.slsa.builders.location.LocationDtoBuilder.getLocationWithoutId;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class LocationScheduleDaoTest {
    @Autowired
    private LocationScheduleDao locationScheduleDao;

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private LocationTypeDao locationTypeDao;

    @Test
    public void saveTest() {
        LocationSchedule locationSchedule = LocationSchedule.builder().location(getLocation()).day(Day.MONDAY).startTime(LocalTime.of(10, 00)).endTime(LocalTime.of(18, 00)).build();
        LocationSchedule newLocationSchedule = locationScheduleDao.save(locationSchedule);
        assertEquals(locationSchedule, newLocationSchedule);
    }

    @Test
    public void updateTest() {
        LocationSchedule locationSchedule = LocationSchedule.builder().location(getLocation()).day(Day.MONDAY).startTime(LocalTime.of(10, 00)).endTime(LocalTime.of(18, 00)).build();

        LocationSchedule newLocationSchedule = locationScheduleDao.save(locationSchedule);
        newLocationSchedule.setDay(Day.FRIDAY);
        LocationSchedule updateLocationSchedule = locationScheduleDao.update(locationSchedule);
        assertEquals(updateLocationSchedule, newLocationSchedule);
    }

    @Test
    public void deleteTest() {
        LocationSchedule locationSchedule = LocationSchedule.builder().location(getLocation()).day(Day.MONDAY).startTime(LocalTime.of(10, 00)).endTime(LocalTime.of(18, 00)).build();
        LocationSchedule newLocationSchedule = locationScheduleDao.save(locationSchedule);
        int id = newLocationSchedule.getId();
        locationScheduleDao.delete(newLocationSchedule);
        assertNull(locationScheduleDao.getById(id));
    }

    @Test
    public void getByLocationIdTest() {
        LocationType locationType = LocationTypeBuilder.getLocationTypeWithoutId();
        LocationType saveLocationType = locationTypeDao.save(locationType);

        Location location = getLocationWithoutId();
        location.setLocationType(saveLocationType);
        Location saveLocation = locationDao.save(location);
        int locationId = saveLocation.getId();

        LocationSchedule locationSchedule = LocationSchedule.builder()
                .location(saveLocation)
                .day(Day.MONDAY)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(18, 0))
                .build();
        LocationSchedule saveLocationSchedule = locationScheduleDao.save(locationSchedule);

        List<LocationSchedule> locationSchedule1 = new ArrayList<>(Collections.singletonList(saveLocationSchedule));
        List<LocationSchedule> locationScheduleByLocationId = locationScheduleDao.getAllByLocationId(locationId);

        assertTrue(locationScheduleByLocationId.containsAll(locationSchedule1));
    }

}
