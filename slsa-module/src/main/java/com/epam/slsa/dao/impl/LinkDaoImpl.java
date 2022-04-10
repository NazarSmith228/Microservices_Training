package com.epam.slsa.dao.impl;

import com.epam.slsa.dao.LinkDao;
import com.epam.slsa.model.Link;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Slf4j
public class LinkDaoImpl implements LinkDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public Link save(Link newElement) {
        log.info("Saving Link: {}", newElement);
        manager.persist(newElement);
        return newElement;
    }

    @Override
    @Transactional
    public Link update(Link updatedElement) {
        log.info("Updating Link id: {}", updatedElement.getId());
        return manager.merge(updatedElement);
    }

    @Override
    @Transactional
    public void delete(Link deletedElement) {
        log.info("Deleting Link id: {}", deletedElement.getId());
        manager.remove(deletedElement);
    }

    @Override
    public Link getById(int id) {
        log.info("Getting Link by id: {}", id);
        return manager.find(Link.class, id);
    }

    @Override
    public Link getByUrl(String url) {
        log.info("Getting Link by url: {}", url);
        return manager.createQuery("SELECT l FROM Link l WHERE l.url = :url", Link.class)
                .setParameter("url", url)
                .getSingleResult();
    }
}