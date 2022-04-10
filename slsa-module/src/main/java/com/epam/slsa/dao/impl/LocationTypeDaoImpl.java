package com.epam.slsa.dao.impl;

import com.epam.slsa.dao.LocationTypeDao;
import com.epam.slsa.model.LocationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Slf4j
public class LocationTypeDaoImpl implements LocationTypeDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public LocationType save(LocationType newElement) {
        log.info("Saving LocationType: {}", newElement);
        manager.persist(newElement);
        return newElement;
    }

    @Override
    @Transactional
    public LocationType update(LocationType updatedElement) {
        log.info("Updating LocationType id: {}", updatedElement.getId());
        return manager.merge(updatedElement);
    }

    @Override
    @Transactional
    public void delete(LocationType deletedElement) {
        log.info("Deleting LocationType id: {}", deletedElement.getId());
        manager.remove(deletedElement);
    }

    @Override
    public LocationType getById(int id) {
        log.info("Getting LocationType by id: {}", id);
        return manager.find(LocationType.class, id);
    }

    @Override
    public List<LocationType> getAll() {
        log.info("Getting List of LocationType");
        return manager.createQuery("SELECT lt FROM LocationType lt", LocationType.class)
                .getResultList();
    }

}
