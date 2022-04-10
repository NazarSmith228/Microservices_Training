package com.epam.spsa.dao;

import com.epam.spsa.model.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback
public class EventDaoTest {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private UserDao userDao;

    @Test
    public void getAllEventTest() {
        List<Event> events = eventDao.getAll();
        Assertions.assertTrue(events.size() > 0);
    }

    @Test
    public void saveEventTest() {
        Event event = getEvent();
        Event newEvent = eventDao.save(event);
        Assertions.assertEquals(event, newEvent);
    }

    @Test
    public void updateEventTest() {
        Event event = eventDao.getAll().get(0);
        event.setLocation_id(1);
        Event editedEvent = eventDao.update(event);
        Assertions.assertEquals(editedEvent, event);
    }

    @Test
    public void getEventById() {
        Event event = eventDao.getAll().get(0);
        Event eventById = eventDao.getById(event.getId());
        Assertions.assertEquals(eventById, event);
    }

    private Event getEvent() {
        return Event.builder()
                .date(LocalDate.parse("2020-10-10"))
                .time(Time.valueOf("10:10:10"))
                .location_id(2)
                .owner(userDao.getById(1))
                .build();
    }

}
