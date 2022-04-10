package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.TagDao;
import com.epam.spsa.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Slf4j
public class TagDaoImpl implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Tag save(Tag tag) {
        log.info("Saving Tag: {}", tag);
        entityManager.persist(tag);
        return tag;
    }

    @Transactional
    @Override
    public void delete(Tag tag) {
        log.info("Deleting Tag: {}", tag);
        entityManager.remove(tag);
    }

    @Override
    public Tag getById(int id) {
        log.info("Getting Tag by id: {}", id);
        return entityManager.find(Tag.class, id);
    }

    @Override
    public List<Tag> getAll() {
        log.info("Getting List of Tag");
        return entityManager.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
    }

    @Override
    public Tag getByName(String name) {
        log.info("Getting Tag by name: {}", name);
        return entityManager.createQuery("SELECT t FROM Tag t WHERE t.name = :name", Tag.class)
                .setParameter("name", name)
                .getSingleResult();
    }

}
