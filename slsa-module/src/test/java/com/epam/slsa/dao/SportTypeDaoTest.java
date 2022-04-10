package com.epam.slsa.dao;

import com.epam.slsa.model.SportType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.slsa.builders.sportType.SportTypeBuilder.getSportTypeWithoutId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
@Rollback
public class SportTypeDaoTest {

    @Autowired
    private SportTypeDao sportTypeDao;

    @Test
    public void saveTest() {
        SportType sportType = getSportTypeWithoutId();
        SportType newSportType = sportTypeDao.save(sportType);

        assertEquals(sportType, newSportType);
    }

    @Test
    public void updateTest() {
        SportType sportType = getSportTypeWithoutId();
        SportType newSportType = sportTypeDao.save(sportType);
        newSportType.setName("Swimming");
        SportType updateSportType = sportTypeDao.update(newSportType);

        assertEquals(updateSportType, newSportType);
    }

    @Test
    public void deleteTest() {
        SportType sportType = getSportTypeWithoutId();
        SportType newSportType = sportTypeDao.save(sportType);
        int id = newSportType.getId();
        sportTypeDao.delete(newSportType);

        assertNull(sportTypeDao.getById(id));
    }

    @Test
    public void getByIdTest() {
        SportType sportType = getSportTypeWithoutId();
        SportType newSportType = sportTypeDao.save(sportType);
        int id = newSportType.getId();
        SportType getByIdSportType = sportTypeDao.getById(id);
        assertEquals(newSportType, getByIdSportType);
    }

    @Test
    public void getAllTest() {
        SportType sportType = getSportTypeWithoutId();
        int sizeBefore = sportTypeDao.getAll().size();
        sportTypeDao.save(sportType);
        int sizeAfter = sportTypeDao.getAll().size();

        assertEquals(1, sizeAfter - sizeBefore);
    }

}
