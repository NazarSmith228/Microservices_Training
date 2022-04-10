package com.epam.spsa.dao;

import com.epam.spsa.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
@Rollback
public class TagDaoTest {

    @Autowired
    private TagDao tagDao;

    @Test
    public void saveTest() {
        Tag tag = Tag.builder().name("Yoga").build();
        Tag newTag = tagDao.save(tag);

        assertEquals(tag, newTag);
    }

    @Test
    public void deleteTest() {
        Tag tag = Tag.builder().name("Yoga").build();
        Tag newTag = tagDao.save(tag);
        int id = newTag.getId();
        tagDao.delete(newTag);

        assertNull(tagDao.getById(id));
    }

    @Test
    public void getByIdTest() {
        Tag tag = Tag.builder().name("Yoga").build();
        Tag newTag = tagDao.save(tag);
        int id = newTag.getId();
        Tag getByIdTag = tagDao.getById(id);

        assertEquals(newTag, getByIdTag);
    }

    @Test
    public void getAllTest() {
        Tag tag = Tag.builder().name("Yoga").build();
        int sizeBefore = tagDao.getAll().size();
        tagDao.save(tag);
        int sizeAfter = tagDao.getAll().size();

        assertEquals(1, sizeAfter - sizeBefore);
    }

    @Test
    public void getByNameTest() {
        Tag tag = Tag.builder().name("Yoga").build();
        Tag newTag = tagDao.save(tag);
        String name = newTag.getName();
        Tag getByIdTag = tagDao.getByName(name);

        assertEquals(newTag, getByIdTag);
    }

}
