package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.FormDao;
import com.epam.spsa.model.Form;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Repository
public class FormDaoImpl implements FormDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public Form save(Form form) {
        log.info("Saving Form, id: {}", form.getId());
        manager.persist(form);
        return form;
    }

    @Override
    public Form getById(int id) {
        log.info("Getting Form by id: {}", id);
        return manager.find(Form.class, id);
    }

    @Override
    public List<Form> getByUserId(int userId) {
        log.info("Getting Form by user id: {}", userId);
        return manager
                .createQuery("SELECT f FROM Form f WHERE f.user.id = :user_id", Form.class)
                .setParameter("user_id", userId)
                .getResultList();
    }

    @Override
    public List<Form> getAll() {
        log.info("Getting list of Forms");
        return manager
                .createQuery("SELECT f FROM Form f", Form.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void delete(Form form) {
        manager.remove(form);
    }

}
