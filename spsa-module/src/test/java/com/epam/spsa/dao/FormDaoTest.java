package com.epam.spsa.dao;

import com.epam.spsa.model.Form;
import com.epam.spsa.model.Role;
import com.epam.spsa.model.User;
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
public class FormDaoTest {

    @Autowired
    private FormDao formDao;

    @Test
    public void saveTest() {
        Form form = getForm();
        formDao.save(form);

        assertTrue(formDao.getAll().size() > 0);
    }

    @Test
    public void getByIdTest() {
        Form form = getForm();

        formDao.save(form);
        Form savedForm = formDao.getById(form.getId());

        assertEquals(form.getLocationId(), savedForm.getLocationId());
        assertEquals(form.getRole(), savedForm.getRole());
    }

    @Test
    public void deleteTest() {
        Form form = getForm();

        formDao.save(form);
        int size = formDao.getAll().size();
        assertTrue(size > 0);

        formDao.delete(form);
        assertTrue(formDao.getAll().size() < size);
    }

    private static Form getForm() {
        return Form.builder()
                .latitude(10.0)
                .longitude(20.0)
                .locationId(1)
                .user(User.builder().id(1).build())
                .locationName("Test")
                .role(getCoachRole())
                .build();
    }

    private static Role getCoachRole() {
        return Role.builder()
                .id(2)
                .name("COACH")
                .build();
    }

}
