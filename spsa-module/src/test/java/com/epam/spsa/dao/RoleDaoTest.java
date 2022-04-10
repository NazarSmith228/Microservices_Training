package com.epam.spsa.dao;

import com.epam.spsa.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Rollback
public class RoleDaoTest {

    @Autowired
    private RoleDao roleDao;

    @Test
    public void saveTest() {
        Role role = getRole();
        roleDao.save(role);

        assertTrue(roleDao.getAll().size() > 0);
    }

    @Test
    public void getByIdTest() {
        Role role = getRole();

        roleDao.save(role);
        Role savedRole = roleDao.getById(role.getId());

        assertEquals(role.getName(), savedRole.getName());
    }

    @Test
    public void getByName() {
        Role role = getRole();

        roleDao.save(role);
        Role savedRole = roleDao.getByName(role.getName());

        assertEquals(role.getId(), savedRole.getId());
    }

    private static Role getRole() {
        return Role.builder()
                .name("TEST_ROLE")
                .build();
    }

}
