package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.SportTypeDao;
import com.epam.spsa.model.SportType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Slf4j
public class SportTypeDaoImpl implements SportTypeDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public SportType save(SportType sportType) {
        log.info("Saving SportType: {}", sportType.getName());
        entityManager.persist(sportType);
        return sportType;
    }

    @Override
    public SportType getById(int id) {
        log.info("Getting SportType by id: {}", id);
        return entityManager.find(SportType.class, id);
    }

    @Override
    public List<SportType> getAll() {
        log.info("Getting List of SportType");
        return entityManager
                .createQuery("SELECT sp FROM SportType sp", SportType.class)
                .getResultList();
    }

}
