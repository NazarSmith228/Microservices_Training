package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.CriteriaDao;
import com.epam.spsa.model.Criteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Slf4j
public class CriteriaDaoImpl implements CriteriaDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Criteria save(Criteria criteria) {
        log.info("Saving Criteria. Criteria.user: {}", criteria.getUser());
        entityManager.persist(criteria);
        return criteria;
    }

    @Override
    public Criteria getById(int id) {
        log.info("Getting Criteria by id: {}", id);
        return entityManager.find(Criteria.class, id);
    }

    @Transactional
    @Override
    public void delete(Criteria criteria) {
        log.info("Deleting Criteria by id: {}", criteria.getId());
        entityManager.remove(criteria);
    }

    @Override
    public List<Criteria> getAll() {
        log.info("Getting List of Criteria");
        return entityManager
                .createQuery("SELECT cr FROM Criteria cr", Criteria.class)
                .getResultList();
    }

    @Override
    public List<Criteria> getByUserId(int id) {
        log.info("Getting List of Criteria by User id: {}", id);
        return entityManager
                .createQuery("SELECT cr FROM Criteria cr WHERE cr.user.id =:id ", Criteria.class)
                .setParameter("id", id)
                .getResultList();
    }

}
