package com.epam.slsa.dao.impl;

import com.epam.slsa.dao.SportTypeDao;
import com.epam.slsa.model.SportType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Slf4j
public class SportTypeDaoImpl implements SportTypeDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public SportType save(SportType newElement) {
        log.info("Saving SportType: {}", newElement);
        manager.persist(newElement);
        return newElement;
    }

    @Override
    @Transactional
    public SportType update(SportType updatedElement) {
        log.info("Updating SportType id: {}", updatedElement.getId());
        return manager.merge(updatedElement);
    }

    @Override
    @Transactional
    public void delete(SportType deletedElement) {
        log.info("Deleting SportType id: {}", deletedElement.getId());
        manager.remove(deletedElement);
    }

    @Override
    public SportType getById(int id) {
        log.info("Getting SportType by id: {}", id);
        return manager.find(SportType.class, id);
    }

    @Override
    public List<SportType> getAll() {
        log.info("Getting List of SportType");
        return manager.createQuery("SELECT s FROM SportType s", SportType.class)
                .getResultList();
    }

}
