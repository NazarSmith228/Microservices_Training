package com.epam.spsa.dao;

import com.epam.spsa.controller.builder.EstimationBuilder;
import com.epam.spsa.model.ArticleComment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback
public class ArticleCommentDaoTest {

    @Autowired
    private ArticleCommentDao articleCommentDao;

    private ArticleComment getComment() {
        return ArticleComment.builder()
                .creationDate(new Timestamp(System.currentTimeMillis()))
                .content("My content")
                .image("https:..")
                .build();
    }

    @BeforeEach
    void saveCom() {
        articleCommentDao.save(getComment());
    }

    @Test
    public void saveTest() {
        ArticleComment newComment = getComment();
        ArticleComment savedComment = articleCommentDao.save(newComment);
        Assertions.assertEquals(newComment, savedComment);
    }

    @Test
    public void getByIdTest() {
        int id = 1;
        ArticleComment comment = articleCommentDao.getById(id);
        Assertions.assertNotNull(comment);
    }

    @Test
    public void getAllTest() {
        List<ArticleComment> comments = articleCommentDao.getAll();
        Assertions.assertTrue(comments.size() > 0);
    }

    @Test
    public void getAllByArticleIdTest() {
        int articleId = 1;
        List<ArticleComment> comments = articleCommentDao.getAllByArticleId(articleId);
        Assertions.assertTrue(comments.size() > 0);
    }

    @Test
    public void getAllByArticleIdASCTest() {
        int articleId = 1;
        List<ArticleComment> comments = articleCommentDao.getAllByArticleIdASC(articleId);
        Assertions.assertTrue(comments.size() > 0);
    }

    @Test
    public void getAllByArticleIdDESKTest() {
        int articleId = 1;
        List<ArticleComment> comments = articleCommentDao.getAllByArticleIdDESK(articleId);
        Assertions.assertTrue(comments.size() > 0);
    }

    @Test
    public void deleteCommentTest() {
        int commentId = 1;
        articleCommentDao.deleteComment(commentId);
        ArticleComment comment = articleCommentDao.getById(commentId);
        Assertions.assertNull(comment);
    }

    @Test
    public void updateTest() {
        int commentId = 1;
        ArticleComment comment = articleCommentDao.getById(commentId);
        comment.setEstimations(new HashSet<>(Collections.singletonList(EstimationBuilder.getDislike())));
        articleCommentDao.update(comment);
        ArticleComment comment1 = articleCommentDao.getById(commentId);
        Assertions.assertEquals(comment.getEstimations(), comment1.getEstimations());
    }

    @Test
    public void getDeletedCommentTest() {
        ArticleComment comment = articleCommentDao.getDeletedComment();
        Assertions.assertNotNull(comment);
    }


}
