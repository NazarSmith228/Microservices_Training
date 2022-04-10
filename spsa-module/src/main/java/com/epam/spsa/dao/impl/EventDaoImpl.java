package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.EventDao;
import com.epam.spsa.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@Repository
public class EventDaoImpl implements EventDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void delete(Event event) {
        log.info("Delete event: {}", event);
        entityManager.remove(event);
    }

    @Transactional
    @Override
    public Event update(Event event) {
        log.info("Update event: {}", event);
        return entityManager.merge(event);
    }

    @Transactional
    @Override
    public Event save(Event event) {
        log.info("Save event: {}", event);
        entityManager.persist(event);
        return event;
    }

    @Override
    public Event getById(int id) {
        log.info("Get event by id: {}", id);
        return entityManager.createQuery("SELECT e FROM Event e WHERE e.id = :id", Event.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<Event> getAll() {
        log.info("Get all events");
        return entityManager.createQuery("SELECT e FROM Event e", Event.class)
                .getResultList();
    }

}
