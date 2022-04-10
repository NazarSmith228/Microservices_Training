package com.epam.slsa.dao.impl;

import com.epam.slsa.dao.AddressDao;
import com.epam.slsa.model.Address;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Slf4j
public class AddressDaoImpl implements AddressDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public Address save(Address newElement) {
        log.info("Saving Address: {}", newElement);
        manager.persist(newElement);
        return newElement;
    }

    @Override
    @Transactional
    public Address update(Address updatedElement) {
        log.info("Updating Address id: {}", updatedElement.getId());
        return manager.merge(updatedElement);
    }

    @Override
    @Transactional
    public void delete(Address deletedElement) {
        log.info("Deleting Address id: {}", deletedElement.getId());
        manager.remove(deletedElement);
    }

    @Override
    public Address getById(int id) {
        log.info("Getting Address by id:{}", id);
        return manager.find(Address.class, id);
    }

    @Override
    public List<Address> getAll() {
        log.info("Getting List of Address");
        return manager.createQuery("SELECT a FROM Address a", Address.class)
                .getResultList();
    }

    @Override
    public Address getByLocationId(int locationId) {
        log.info("Getting Address by LocationId: {}", locationId);
        return manager.createQuery("SELECT a FROM Address a WHERE a.location.id = :location_id", Address.class)
                .setParameter("location_id", locationId)
                .getSingleResult();
    }

    @Override
    public Address getByCoordinates(double latitude, double longitude) {
        return manager
                .createQuery("SELECT a FROM Address a WHERE a.latitude = :latitude AND a.longitude = :longitude", Address.class)
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .getSingleResult();
    }

}
