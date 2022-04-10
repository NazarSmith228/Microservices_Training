package com.epam.slsa.dao;

import com.epam.slsa.model.LocationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.slsa.builders.locationType.LocationTypeBuilder.getLocationTypeWithoutId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
@Rollback
public class LocationTypeDaoTest {

    @Autowired
    private LocationTypeDao locationTypeDao;

    @Test
    public void saveTest() {
        LocationType locationType = getLocationTypeWithoutId();
        LocationType saveLocationType = locationTypeDao.save(locationType);

        assertEquals(locationType, saveLocationType);
    }

    @Test
    public void updateTest() {
        LocationType locationType = getLocationTypeWithoutId();
        LocationType newLocationType = locationTypeDao.save(locationType);
        newLocationType.setName("Gym");
        LocationType updateLocationType = locationTypeDao.update(newLocationType);

        assertEquals(updateLocationType, newLocationType);
    }

    @Test
    public void deleteTest() {
        LocationType locationType = getLocationTypeWithoutId();
        LocationType newLocationType = locationTypeDao.save(locationType);
        int id = newLocationType.getId();
        locationTypeDao.delete(newLocationType);

        assertNull(locationTypeDao.getById(id));
    }

    @Test
    public void getByIdTest() {
        LocationType locationType = getLocationTypeWithoutId();
        LocationType newLocationType = locationTypeDao.save(locationType);
        int id = newLocationType.getId();
        LocationType getByIdLocationType = locationTypeDao.getById(id);
        assertEquals(newLocationType, getByIdLocationType);
    }

    @Test
    public void getAllTest() {
        LocationType locationType = getLocationTypeWithoutId();
        int sizeBefore = locationTypeDao.getAll().size();
        locationTypeDao.save(locationType);
        int sizeAfter = locationTypeDao.getAll().size();

        assertEquals(1, sizeAfter - sizeBefore);
    }

}
