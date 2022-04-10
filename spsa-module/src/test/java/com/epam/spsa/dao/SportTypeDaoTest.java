package com.epam.spsa.dao;

import com.epam.spsa.controller.builder.SportTypeDtoBuilder;
import com.epam.spsa.model.SportType;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class SportTypeDaoTest {

    @Autowired
    private SportTypeDao sportTypeDao;

    @Autowired
    private ModelMapper mapper;

    @Test
    public void saveSportTypeTest() {
        SportType sportType = getSportType();
        SportType saved = sportTypeDao.save(sportType);
        assertEquals(saved, sportType);
    }

    @Test
    public void getSportTypeById() {
        int id = 1;
        SportType sportType = sportTypeDao.getById(id);
        assertNotNull(sportType);
    }

    @Test
    public void getAllSportTypesTest() {
        List<SportType> sportTypes = sportTypeDao.getAll();
        assertTrue(sportTypes.size() > 0);
    }

    private SportType getSportType() {
        return mapper.map(SportTypeDtoBuilder.getSportTypeDto(), SportType.class);
    }

}