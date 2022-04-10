package com.epam.slsa.dao;

import com.epam.slsa.model.Location;
import com.epam.slsa.model.LocationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.slsa.builders.location.LocationDtoBuilder.getLocationWithoutId;
import static com.epam.slsa.builders.locationType.LocationTypeBuilder.getLocationTypeWithoutId;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class LocationDaoTest {
    @Autowired
    private LocationDao locationDao;

    @Autowired
    private LocationTypeDao locationTypeDao;

    @Test
    public void saveTest() {
        LocationType locationType = getLocationTypeWithoutId();
        LocationType saveLocationType = locationTypeDao.save(locationType);
        Location location = getLocationWithoutId();
        location.setLocationType(saveLocationType);
        Location newLocation = locationDao.save(location);

        assertEquals(location, newLocation);
    }

    @Test
    public void updateTest() {
        LocationType locationType = getLocationTypeWithoutId();
        LocationType saveLocationType = locationTypeDao.save(locationType);
        Location location = getLocationWithoutId();
        location.setLocationType(saveLocationType);
        Location newLocation = locationDao.save(location);
        newLocation.setName("Park");
        Location updateLocation = locationDao.update(newLocation);

        assertEquals(updateLocation, newLocation);
    }

    @Test
    public void deleteTest() {
        LocationType locationType = getLocationTypeWithoutId();
        LocationType saveLocationType = locationTypeDao.save(locationType);
        Location location = getLocationWithoutId();
        location.setLocationType(saveLocationType);
        Location newLocation = locationDao.save(location);
        int id = newLocation.getId();
        locationDao.delete(newLocation);

        assertNull(locationDao.getById(id));
    }

    @Test
    public void getByIdTest() {
        LocationType locationType = getLocationTypeWithoutId();
        LocationType saveLocationType = locationTypeDao.save(locationType);
        Location location = getLocationWithoutId();
        location.setLocationType(saveLocationType);
        Location newLocation = locationDao.save(location);
        int id = newLocation.getId();
        Location getByIdLocation = locationDao.getById(id);

        assertEquals(newLocation, getByIdLocation);
    }

    @Test
    public void getAllTest() {
        LocationType locationType = getLocationTypeWithoutId();
        LocationType saveLocationType = locationTypeDao.save(locationType);
        Location location = getLocationWithoutId();
        location.setLocationType(saveLocationType);
        int sizeBefore = locationDao.getAll().size();
        locationDao.save(location);
        int sizeAfter = locationDao.getAll().size();

        assertEquals(1, sizeAfter - sizeBefore);
    }

    @Test
    public void getByNameTest() {
        LocationType locationType = getLocationTypeWithoutId();
        LocationType saveLocationType = locationTypeDao.save(locationType);

        Location location = getLocationWithoutId();
        String name = location.getName();
        location.setLocationType(saveLocationType);
        Location saveLocation = locationDao.save(location);

        List<Location> locations = new ArrayList<>(Collections.singletonList(saveLocation));
        List<Location> locationsByName = locationDao.getByName(name);

        assertTrue(locationsByName.containsAll(locations));
    }

}
