package com.epam.slsa.dao.impl;

import com.epam.slsa.dao.LocationDao;
import com.epam.slsa.model.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Slf4j
public class LocationDaoImpl implements LocationDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public Location save(Location newElement) {
        log.info("Saving Location: {}", newElement);
        manager.persist(newElement);
        return newElement;
    }

    @Override
    @Transactional
    public Location update(Location updatedElement) {
        log.info("Updating Location id: {}", updatedElement.getId());
        return manager.merge(updatedElement);
    }

    @Override
    @Transactional
    public void delete(Location deletedElement) {
        log.info("Deleting Location id: {}", deletedElement.getId());
        manager.remove(deletedElement);
    }

    @Override
    public Location getById(int id) {
        log.info("Getting Location by id: {}", id);
        return manager.find(Location.class, id);
    }

    @Override
    public List<Location> getAll() {
        log.info("Getting List of Location");
        return manager.createQuery("SELECT l FROM Location l", Location.class)
                .getResultList();
    }

    @Override
    public List<Location> getByName(String name) {
        log.info("Getting List of Location by name: {}", name);
        return manager.createQuery("SELECT l FROM Location l WHERE l.name = :name", Location.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public Location getByPhoneNumber(String phoneNumber) {
        log.info("Getting Location by phone: {}", phoneNumber);
        return manager.createQuery("SELECT l FROM Location l WHERE l.phoneNumber = :phone_number", Location.class)
                .setParameter("phone_number", phoneNumber)
                .getSingleResult();
    }

    @Override
    public List<Location> getByAdminId(int adminId) {
        log.info("Getting List of Location by adminId: {}", adminId);
        return manager.createQuery("SELECT l FROM Location l WHERE l.adminId = :admin_id", Location.class)
                .setParameter("admin_id", adminId)
                .getResultList();
    }

}
