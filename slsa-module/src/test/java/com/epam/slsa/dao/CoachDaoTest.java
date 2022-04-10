package com.epam.slsa.dao;

import com.epam.slsa.model.Coach;
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

import static com.epam.slsa.builders.coach.CoachDtoBuilder.getCoach;
import static com.epam.slsa.builders.location.LocationDtoBuilder.getLocationWithoutId;
import static com.epam.slsa.builders.locationType.LocationTypeBuilder.getLocationTypeWithoutId;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class CoachDaoTest {

    @Autowired
    private CoachDao coachDao;

    @Autowired
    private LocationTypeDao locationTypeDao;

    @Autowired
    private LocationDao locationDao;

    @Test
    public void saveTest() {
        Coach coach = getCoach();
        Coach newCoach = coachDao.save(coach);

        assertEquals(coach, newCoach);
    }

    @Test
    public void updateTest() {
        Coach coach = getCoach();
        Coach newCoach = coachDao.save(coach);
        newCoach.setRating(4.2);
        Coach updateCoach = coachDao.update(newCoach);

        assertEquals(updateCoach, newCoach);
    }

    @Test
    public void deleteTest() {
        Coach coach = getCoach();
        Coach newCoach = coachDao.save(coach);
        int id = newCoach.getId();
        coachDao.delete(newCoach);

        assertNull(coachDao.getById(id));
    }

    @Test
    public void getByIdTest() {
        Coach coach = getCoach();
        Coach newCoach = coachDao.save(coach);
        int id = newCoach.getId();
        Coach getByIdCoach = coachDao.getById(id);

        assertEquals(newCoach, getByIdCoach);
    }

    @Test
    public void getByUserIdTest() {
        Coach coach = getCoach();
        Coach newCoach = coachDao.save(coach);
        Coach getByUserIdCoach = coachDao.getByUserId(1);

        assertEquals(newCoach, getByUserIdCoach);
    }

    @Test
    public void getAllTest() {
        Coach coach = getCoach();
        int sizeBefore = coachDao.getAll().size();
        coachDao.save(coach);
        int sizeAfter = coachDao.getAll().size();

        assertEquals(1, sizeAfter - sizeBefore);
    }

    @Test
    public void getAllByLocationIdTest() {
        LocationType locationType = getLocationTypeWithoutId();
        LocationType saveLocationType = locationTypeDao.save(locationType);

        Location location = getLocationWithoutId();
        location.setLocationType(saveLocationType);
        Location saveLocation = locationDao.save(location);
        int locationId = saveLocation.getId();

        Coach coach = getCoach();
        coach.setLocation(saveLocation);
        Coach saveCoach = coachDao.save(coach);

        List<Coach> coaches = new ArrayList<>(Collections.singletonList(saveCoach));
        List<Coach> coachesByLocationId = coachDao.getAllByLocationId(locationId);

        assertTrue(coachesByLocationId.containsAll(coaches));
    }

    @Test
    public void getByIdAndLocationIdTest() {
        LocationType locationType = getLocationTypeWithoutId();
        LocationType saveLocationType = locationTypeDao.save(locationType);

        Location location = getLocationWithoutId();
        location.setLocationType(saveLocationType);
        Location saveLocation = locationDao.save(location);
        int locationId = saveLocation.getId();

        Coach coach = getCoach();
        coach.setLocation(saveLocation);
        Coach saveCoach = coachDao.save(coach);
        int coachId = saveCoach.getId();

        Coach coachesByIdAndLocationId = coachDao.getByIdAndLocationId(coachId, locationId);

        assertEquals(saveCoach, coachesByIdAndLocationId);
    }

}
