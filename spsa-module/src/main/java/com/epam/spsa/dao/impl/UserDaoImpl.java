package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.UserDao;
import com.epam.spsa.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Slf4j
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public User save(User user) {
        log.info("Saving User: {}", user);
        entityManager.persist(user);
        return user;
    }

    @Override
    public User getById(int id) {
        log.info("Getting User by id: {}", id);
        return entityManager.find(User.class, id);
    }

    @Transactional
    @Override
    public User update(User user) {
        log.info("Updating User: {}", user);
        entityManager.merge(user);
        return user;
    }

    @Transactional
    @Override
    public void delete(User user) {
        log.info("Deleting User: {}", user);
        entityManager.remove(user);
    }

    @Override
    public List<User> getAll() {
        log.info("Getting List of User");
        return entityManager
                .createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    @Override
    public User getByEmail(String email) {
        log.info("Getting user by email: {}", email);
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        log.info("Getting user by phone number: {}", phoneNumber);
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber", User.class)
                .setParameter("phoneNumber", phoneNumber)
                .getSingleResult();
    }

    @Override
    public User getByProviderId(String providerId) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.providerId = :provider_id", User.class)
                .setParameter("provider_id", providerId)
                .getSingleResult();
    }

}
