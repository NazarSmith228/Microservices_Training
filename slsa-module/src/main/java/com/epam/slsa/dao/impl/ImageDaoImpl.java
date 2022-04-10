package com.epam.slsa.dao.impl;

import com.epam.slsa.dao.ImageDao;
import com.epam.slsa.model.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Slf4j
public class ImageDaoImpl implements ImageDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public Image save(Image newElement) {
        log.info("Saving Photo: {}", newElement);
        manager.persist(newElement);
        return newElement;
    }

    @Override
    @Transactional
    public Image update(Image updatedElement) {
        log.info("Updating Photo id: {}", updatedElement.getId());
        return manager.merge(updatedElement);
    }

    @Override
    @Transactional
    public void delete(Image deletedElement) {
        log.info("Deleting Photo id: {}", deletedElement.getId());
        manager.remove(deletedElement);
    }

    @Override
    public Image getById(int id) {
        log.info("Getting Photo by id: {}", id);
        return manager.find(Image.class, id);
    }
}
