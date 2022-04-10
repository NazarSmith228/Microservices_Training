package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.EstimationDao;
import com.epam.spsa.model.Estimation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Slf4j
public class EstimationDaoImpl implements EstimationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void delete(Estimation estimation) {
        log.info("Deleting Estimation: {}", estimation);
        entityManager.remove(estimation);
    }

    @Transactional
    @Override
    public Estimation update(Estimation estimation) {
        log.info("Updating Estimation: {}", estimation);
        return entityManager.merge(estimation);
    }

}
