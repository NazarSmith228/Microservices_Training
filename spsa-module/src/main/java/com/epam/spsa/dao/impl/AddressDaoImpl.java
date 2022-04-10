package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.AddressDao;
import com.epam.spsa.model.Address;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Slf4j
public class AddressDaoImpl implements AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Address save(Address address) {
        log.info("Saving Address: {}", address);
        entityManager.persist(address);
        return address;
    }

    @Override
    public Address getById(int id) {
        log.info("Getting Address by id: {}", id);
        return entityManager.find(Address.class, id);
    }

    @Override
    public List<Address> getAll() {
        log.info("Getting List of Address");
        return entityManager.createQuery("SELECT a FROM Address a", Address.class)
                .getResultList();
    }

    @Override
    public List<Address> getByCoordinates(double latitude, double longitude) {
        log.info("Getting Address(es) by latitude: {}, longitude: {}", latitude, longitude);
        return entityManager
                .createQuery("SELECT a FROM Address a WHERE a.latitude = :latitude AND a.longitude = :longitude", Address.class)
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .getResultList();
    }

    @Override
    public Address getByUserId(int id) {
        log.info("Getting Address by User id: {}", id);
        return entityManager
                .createQuery("SELECT a FROM Address a WHERE a.user.id = :id", Address.class)
                .setParameter("id", id)
                .getSingleResult();
    }

}
