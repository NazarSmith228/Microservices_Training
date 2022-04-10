package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.MessageDao;
import com.epam.spsa.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Slf4j
public class MessageDaoImpl implements MessageDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void delete(Message message) {
        log.info("Deleting Message: {}", message);
        entityManager.remove(entityManager.contains(message) ? message : entityManager.merge(message));
    }

    @Transactional
    @Override
    public Message update(Message message) {
        log.info("Updating Message: {}", message);
        return entityManager.merge(message);
    }

    @Transactional
    @Override
    public Message save(Message message) {
        log.info("Saving Message: {}", message);
        entityManager.persist(message);
        return message;
    }

    @Override
    public Message getById(int id) {
        log.info("Getting Message by id: {}", id);
        return entityManager.find(Message.class, id);
    }

    @Override
    public List<Message> getAll() {
        log.info("Getting List of Message");
        return entityManager
                .createQuery("SELECT m FROM Message m", Message.class)
                .getResultList();
    }

}
