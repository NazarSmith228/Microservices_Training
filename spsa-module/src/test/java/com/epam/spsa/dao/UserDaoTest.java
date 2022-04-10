package com.epam.spsa.dao;

import com.epam.spsa.controller.builder.UserBuilder;
import com.epam.spsa.model.User;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ModelMapper mapper;

    @Test
    public void saveUserTest() {
        User user = getUser();
        int sizeBefore = userDao.getAll().size();
        userDao.save(user);
        int sizeAfter = userDao.getAll().size();
        assertEquals(1, sizeAfter - sizeBefore);
    }

    @Test
    public void getUserByIdTest() {
        int wrongId = -1;
        int correctId = 1;
        User wrongUser = userDao.getById(wrongId);
        User correctUser = userDao.getById(correctId);
        assertNull(wrongUser);
        assertNotNull(correctUser);
    }

    @Test
    public void deleteUserTest() {
        int sizeBefore = userDao.getAll().size();
        User user = getUser();
        userDao.save(user);
        userDao.delete(user);
        int sizeAfter = userDao.getAll().size();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    public void getUsersByEmailAndPhoneNumberTest() {
        String email = "user@gmail.com";
        String phoneNumber = "0983412832";
        User first = userDao.getByEmail(email);
        User second = userDao.getByPhoneNumber(phoneNumber);
        assertEquals(first, second);
    }

    @Test
    public void updateUserTest() {
        int id = 1;
        User user = userDao.getAll().get(0);
        user.setSurname("NewSurname");
        user.setName("NewName");
        userDao.update(user);
        User updated = userDao.getById(id);
        assertTrue(updated.getName().equals(user.getName()) && updated.getSurname().equals(user.getSurname()));
    }

    private User getUser() {
        return mapper.map(UserBuilder.getUserDto(), User.class);
    }

}