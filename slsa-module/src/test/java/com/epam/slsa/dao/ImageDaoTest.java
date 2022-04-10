package com.epam.slsa.dao;

import com.epam.slsa.model.Image;
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
public class ImageDaoTest {

    @Autowired
    private ImageDao imageDao;

    @Test
    public void getById() {
        Image image = Image.builder()
                .url("https....")
                .build();
        imageDao.save(image);
        image = imageDao.getById(image.getId());
        assertEquals("https....", image.getUrl());
    }

    @Test
    public void saveTest() {
        Image image = Image.builder()
                .url("https....")
                .build();
        Image newImage = imageDao.save(image);

        assertEquals(image, newImage);
    }

    @Test
    public void updateTest() {
        Image image = Image.builder()
                .url("https....")
                .build();
        Image newImage = imageDao.save(image);
        newImage.setUrl("http...");
        newImage = imageDao.update(newImage);
        assertEquals(image, newImage);
    }

    @Test
    public void deleteTest() {
        Image image = Image.builder()
                .url("https....")
                .build();
        Image newImage = imageDao.save(image);
        imageDao.delete(newImage);

        assertNull(imageDao.getById(1));
    }

}
