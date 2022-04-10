package com.epam.spsa.dao;


import com.epam.spsa.model.Chat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class ChatDaoTest {

    @Autowired
    private ChatDao chatDao;

    @Test
    public void getChatByIdTest() {
        int wrongId = -1;
        Chat wrongChat = chatDao.getById(wrongId);
        assertNull(wrongChat);
    }

    @Test
    public void saveChatTest() {
        int beforeSize = chatDao.getAll().size();
        Chat chat = new Chat();
        chatDao.save(chat);
        int afterSize = chatDao.getAll().size();
        assertEquals(1, afterSize - beforeSize);
    }

    @Test
    public void deleteChatTest(){
        int beforeSize = chatDao.getAll().size();
        Chat chat = new Chat();
        chatDao.save(chat);
        chatDao.delete(chat);
        int afterSize = chatDao.getAll().size();
        assertEquals(beforeSize, afterSize);
    }

}
