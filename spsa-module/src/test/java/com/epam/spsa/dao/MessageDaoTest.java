package com.epam.spsa.dao;

import com.epam.spsa.controller.builder.MessagingBuilder;
import com.epam.spsa.model.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class MessageDaoTest {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private ChatDao chatDao;

    @Autowired
    private UserDao userDao;

    @Test
    public void saveMessageTest() {
        Message message = MessagingBuilder.getMessage();
        chatDao.save(message.getChat());
        userDao.save(message.getSender());
        message = messageDao.save(message);
        assertNotNull(message);
        assertTrue(messageDao.getAll().size() > 0);
    }

    @Test
    public void deleteMessageTest() {
        Message message = MessagingBuilder.getMessage();
        chatDao.save(message.getChat());
        userDao.save(message.getSender());
        messageDao.save(message);
        assertEquals(1, messageDao.getAll().size());
        messageDao.delete(message);
        assertEquals(0, messageDao.getAll().size());
    }

    @Test
    public void getMessageByIdTest() {
        int wrongId = -1;
        assertNull(messageDao.getById(wrongId));
    }

    @Test
    public void updateMessageTest() {
        Message message = MessagingBuilder.getMessage();
        chatDao.save(message.getChat());
        userDao.save(message.getSender());
        Message updated = messageDao.save(message);
        updated.setMessage("New message");
        message = messageDao.update(updated);
        assertEquals(message, updated);
    }

}
