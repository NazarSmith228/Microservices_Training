package com.epam.spsa.dao;

import com.epam.spsa.model.Article;
import com.epam.spsa.model.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@Transactional
@Rollback
class ArticleDaoTest {

    @Autowired
    private ArticleDao articleDao;

    @Test
    void saveTest() {
        Article article = getArticle();

        Article result = articleDao.save(article);
        Assertions.assertEquals(article, result);
    }

    @Test
    void getByIdTest() {
        Article result = articleDao.getById(1);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
    }

    @Test
    void getAllTest() {
        List<Article> result = articleDao.getAll();
        Assertions.assertNotNull(result);
    }

    @Test
    void getAllNoContent() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class,
                () -> articleDao.getAllNoContent());
    }

    @Test
    void updateIncorrectTest() {
        Article article = getArticle();
        Assertions.assertThrows(EmptyResultDataAccessException.class,
                () -> articleDao.update(article));
    }

    @Test
    void getArticleByTopicTest() {
        Article article = getArticle();
        articleDao.save(article);

        Article result = articleDao.getArticleByTopic(article.getTopic());

        Assertions.assertEquals(article, result);
        Assertions.assertEquals(article.getTopic(), result.getTopic());
    }

    private Article getArticle() {
        Set<Tag> tags = new HashSet<>();
        tags.add(getTag());
        return Article.builder()
                .authorName("Name")
                .authorSurname("Secondname")
                .topic("topic")
                .description("description")
                .pictureUrl("http://localhost:8080")
                .creationDate(LocalDate.parse("2020-03-17"))
                .content("content")
                .tags(tags)
                .build();
    }

    private Tag getTag() {
        return Tag.builder()
                .name("Yoga")
                .build();
    }
}