package com.epam.slsa.dao.impl;

import com.epam.slsa.dao.CoachDao;
import com.epam.slsa.model.Coach;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Slf4j
public class CoachDaoImpl implements CoachDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public Coach save(Coach newElement) {
        log.info("Saving Coach: {}", newElement);
        manager.persist(newElement);
        return newElement;
    }

    @Override
    @Transactional
    public Coach update(Coach updatedElement) {
        log.info("Updating Coach id: {}", updatedElement.getId());
        return manager.merge(updatedElement);
    }

    @Override
    @Transactional
    public void delete(Coach deletedElement) {
        log.info("Deleting Coach id: {}", deletedElement.getId());
        manager.remove(deletedElement);
    }

    @Override
    public Coach getById(int id) {
        log.info("Getting Coach by id: {}", id);
        return manager.find(Coach.class, id);
    }

    @Override
    public List<Coach> getAll() {
        log.info("Getting List of Coach");
        return manager.createQuery("SELECT c FROM Coach c", Coach.class)
                .getResultList();
    }

    @Override
    public List<Coach> getAllByLocationId(int id) {
        log.info("Getting List of Coach by LocationId: {}", id);
        return manager.createQuery("SELECT c FROM Coach c WHERE c.location.id = :id", Coach.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Coach getByUserId(int id) {
        log.info("Getting Coach by userId {}", id);
        return manager.createQuery("SELECT c FROM Coach c WHERE c.userId = :user_id", Coach.class)
                .setParameter("user_id", id)
                .getSingleResult();
    }

    @Override
    public Coach getByIdAndLocationId(int coachId, int locationId) {
        log.info("Getting Coach by CoachId: {} and LocationId: {}", coachId, locationId);
        return manager.createQuery("SELECT c FROM Coach c WHERE c.location.id = :location_id AND c.id = :coach_id", Coach.class)
                .setParameter("location_id", locationId)
                .setParameter("coach_id", coachId)
                .getSingleResult();
    }

}
