package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.RoleDao;
import com.epam.spsa.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Role save(Role role) {
        manager.persist(role);
        return role;
    }

    @Override
    public Role getById(int id) {
        return manager.find(Role.class, id);
    }

    @Override
    public Role getByName(String name) {
        return manager
                .createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Override
    public List<Role> getAll() {
        return manager
                .createQuery("SELECT r FROM Role r", Role.class)
                .getResultList();
    }

}
