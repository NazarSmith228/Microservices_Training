package com.epam.spsa.dao;

import com.epam.spsa.model.ActivityTime;
import com.epam.spsa.model.Criteria;
import com.epam.spsa.model.Gender;
import com.epam.spsa.model.Maturity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@Rollback
public class CriteriaDaoTest {

    @Autowired
    private CriteriaDao criteriaDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SportTypeDao sportTypeDao;

    @Test
    public void saveCriteriaTest() {
        criteriaDao.save(getCriteria());
        assertEquals(54, criteriaDao.getAll().size());
    }

    @Test
    public void getCriteriaByUserIdTest() {
        int id = 2;
        List<Criteria> criteriaList = criteriaDao.getByUserId(id);
        assertEquals(10, criteriaList.size());
    }

    @Test
    public void deleteCriteriaTest() {
        int sizeBefore = criteriaDao.getAll().size();
        criteriaDao.delete(criteriaDao.getAll().get(0));
        int sizeAfter = criteriaDao.getAll().size();
        assertEquals(1, sizeBefore - sizeAfter);
    }

    @Test
    public void getCriteriaById() {
        int id = 2;
        Criteria criteria = criteriaDao.getById(id);
        assertNotNull(criteria);
    }

    private Criteria getCriteria() {
        int id = 1;
        return Criteria.builder()
                .activityTime(ActivityTime.ALL)
                .maturity(Maturity.PRO)
                .gender(Gender.MALE)
                .sportType(sportTypeDao.getById(id))
                .runningDistance(7)
                .user(userDao.getById(id))
                .build();
    }

}