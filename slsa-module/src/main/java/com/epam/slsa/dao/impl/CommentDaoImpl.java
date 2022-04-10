package com.epam.slsa.dao.impl;

import com.epam.slsa.dao.CommentDao;
import com.epam.slsa.model.Comment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Slf4j
public class CommentDaoImpl implements CommentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Comment save(Comment newElement) {
        log.info("Saving Comment: {}", newElement);
        entityManager.persist(newElement);
        return newElement;
    }

    @Transactional
    @Override
    public Comment update(Comment updatedElement) {
        log.info("Updating Comment id: {}", updatedElement.getId());
        return entityManager.merge(updatedElement);
    }

    @Transactional
    @Override
    public void delete(Comment deletedElement) {
        log.info("Deleting Comment id: {}", deletedElement.getId());
        entityManager.remove(deletedElement);
    }

    @Override
    public Comment getById(int id) {
        log.info("Getting Comment by id: {}", id);
        return entityManager.find(Comment.class, id);
    }

    @Override
    public List<Comment> getAll() {
        log.info("Getting List of Comments");
        return entityManager.createQuery("SELECT c FROM Comment c", Comment.class)
                .getResultList();
    }
}
