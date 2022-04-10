package com.epam.slsa.dao.impl;

import com.epam.slsa.dao.LocationScheduleDao;
import com.epam.slsa.model.LocationSchedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Slf4j
public class LocationScheduleDaoImpl implements LocationScheduleDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public LocationSchedule save(LocationSchedule newElement) {
        log.info("Saving LocationSchedule: {}", newElement);
        manager.persist(newElement);
        return newElement;
    }

    @Override
    @Transactional
    public LocationSchedule update(LocationSchedule updatedElement) {
        log.info("Updating LocationSchedule id: {}", updatedElement.getId());
        return manager.merge(updatedElement);
    }

    @Override
    @Transactional
    public void delete(LocationSchedule deletedElement) {
        log.info("Deleting LocationSchedule id: {}", deletedElement.getId());
        manager.remove(deletedElement);
    }

    @Override
    public LocationSchedule getById(int id) {
        log.info("Getting LocationSchedule by id: {}", id);
        return manager.find(LocationSchedule.class, id);
    }

    @Override
    public List<LocationSchedule> getAllByLocationId(int locationId) {
        log.info("Getting List of LocationSchedule by LocationId: {}", locationId);
        return manager.createQuery("SELECT l FROM LocationSchedule l WHERE l.location.id = :location_id", LocationSchedule.class)
                .setParameter("location_id", locationId)
                .getResultList();
    }

}
