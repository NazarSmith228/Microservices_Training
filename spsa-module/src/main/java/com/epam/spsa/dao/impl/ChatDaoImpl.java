package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.ChatDao;
import com.epam.spsa.model.Chat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Slf4j
public class ChatDaoImpl implements ChatDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void delete(Chat chat) {
        log.info("Deleting Chat: {}", chat);
        entityManager.remove(chat);
    }

    @Transactional
    @Override
    public Chat save(Chat chat) {
        log.info("Saving Chat: {}", chat);
        entityManager.persist(chat);
        return chat;
    }

    @Override
    public Chat getById(int id) {
        log.info("Getting Chat by id: {}", id);
        return entityManager.find(Chat.class, id);
    }

    @Override
    public List<Chat> getAll() {
        log.info("Getting List of Chat");
        return entityManager
                .createQuery("SELECT c FROM Chat c", Chat.class)
                .getResultList();
    }

}
