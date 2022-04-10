package com.epam.spsa.dao;

import com.epam.spsa.controller.builder.UserBuilder;
import com.epam.spsa.model.UserStats;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class UserStatsDaoTest {

    @Autowired
    private UserStatsDao statsDao;

    @Test
    public void saveTest() {
        UserStats stats = UserBuilder.getUserStats();

        UserStats result = statsDao.save(stats);

        assertNotNull(result);
        assertEquals(stats, result);
    }

    @Test
    public void getByIdTest() {
        UserStats stats = UserBuilder.getUserStats();
        statsDao.save(stats);

        UserStats result = statsDao.getById(1);

        assertNotNull(result);
    }

    @Test
    public void getAllTest() {
        UserStats stats = UserBuilder.getUserStats();
        statsDao.save(stats);
        List<UserStats> result = statsDao.getAll();

        assertNotNull(result);
        assertTrue(result.size() >= 1);
    }

    @Test
    public void getAllByUserIdTest() {
        UserStats stats = UserBuilder.getUserStats();

        statsDao.save(stats);
        List<UserStats> result = statsDao.getAllByUserId(1);

        assertNotNull(result);
        assertNotNull(result);
        assertTrue(result.size() >= 1);
    }

    @Test
    public void getLastStatus() {
        UserStats stats = UserBuilder.getUserStats();
        statsDao.save(stats);

        UserStats result = statsDao.getLastStats(1);

        assertNotNull(result);
        assertEquals(stats, result);
    }

    @Test
    public void deleteByUserIdTest() {
        UserStats stats = UserBuilder.getUserStats();
        statsDao.save(stats);

        statsDao.deleteByUserId(1);

        assertThrows(EmptyResultDataAccessException.class, () -> statsDao.getLastStats(1));
    }

    @Test
    public void updateStatsTest() {
        UserStats stats = UserBuilder.getUserStats();
        UserStats editedStats = UserBuilder.getUserStats();
        editedStats.setResultKm(15.5);

        statsDao.save(stats);

        UserStats test = statsDao.updateStats(editedStats);

        assertNotNull(test);
        assertEquals(editedStats, test);
    }

}