package com.epam.spsa.dao;

import com.epam.spsa.controller.builder.ArticleCommentBuilder;
import com.epam.spsa.controller.builder.EstimationBuilder;
import com.epam.spsa.controller.builder.MessagingBuilder;
import com.epam.spsa.model.ArticleComment;
import com.epam.spsa.model.Estimation;
import com.epam.spsa.model.Message;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Rollback
public class EstimationDaoTest {

    @Autowired
    private EstimationDao estimationDao;

    @Autowired
    private ArticleCommentDao articleCommentDao;

    @Test
    public void updateEstimationTest() {
        Estimation estimation = EstimationBuilder.getLike();
        ArticleComment comment = ArticleCommentBuilder.getArticleComment(0);
        Set<Estimation> estimations = new HashSet<>(Collections.singletonList(estimation));
        articleCommentDao.save(comment);
        comment.setEstimations(estimations);
        articleCommentDao.update(comment);
        estimation.setValue(-1);
        Estimation updated = estimationDao.update(estimation);
        assertEquals(estimation.getValue(), updated.getValue());
    }

}
