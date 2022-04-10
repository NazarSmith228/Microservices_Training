package com.epam.slsa.dao;

import com.epam.slsa.model.Link;
import com.epam.slsa.model.LinkType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.slsa.builders.link.LinkBuilder.getLink;

@SpringBootTest
@Transactional
@Rollback
public class LinkDaoTest {

    @Autowired
    private LinkDao linkDao;

    @Test
    public void saveLinkTest(){

        Link link = getLink();
        Link newLink = linkDao.save(link);

        Assertions.assertEquals(newLink,link);
    }

    @Test
    public void updateLinkTest() {
        Link newLink = linkDao.save(getLink());
        newLink.setType(LinkType.FACEBOOK);
        Link updateLink = linkDao.update(newLink);

        Assertions.assertEquals(updateLink, newLink);
    }

    @Test
    public void getLinkByUrlTest(){
        Link oldLink = linkDao.save(getLink());
        Link link = linkDao.getByUrl(oldLink.getUrl());
        Assertions.assertEquals(link.getType(),oldLink.getType());
    }

    @Test
    public void getLinkByIdTest(){
        Link oldLink = linkDao.save(getLink());
        Link link = linkDao.getById(oldLink.getId());
        Assertions.assertEquals(link.getType(),oldLink.getType());
    }

}
