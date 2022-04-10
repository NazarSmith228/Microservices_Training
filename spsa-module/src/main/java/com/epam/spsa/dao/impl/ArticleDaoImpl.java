package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.ArticleDao;
import com.epam.spsa.model.Article;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Slf4j
public class ArticleDaoImpl implements ArticleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Article save(Article article) {
        log.info("Saving Article, id: {}", article.getId());
        entityManager.persist(article);
        return article;
    }

    @Override
    public Article getById(int id) {
        log.info("Getting Article by id: {}", id);
        return entityManager.find(Article.class, id);
    }

    @Override
    public List<Article> getAll() {
        log.info("Getting list of Article");
        return entityManager
                .createQuery("SELECT a FROM Article a", Article.class)
                .getResultList();
    }

    @Transactional
    @Override
    public void delete(Article article) {
        log.info("Deleting Article: {}", article);
        entityManager.remove(article);
    }

    @Transactional
    @Override
    public Article update(Article article) {
        log.info("Updating Article, id: {}", article.getId());
        getArticleByTopic(article.getTopic());
        entityManager.merge(article);
        return article;
    }

    @Override
    public Article getArticleByTopic(String topic) {
        log.info("Getting Article by topic: {}", topic);
        return entityManager.createQuery("SELECT a FROM Article a WHERE a.topic =: topic", Article.class)
                .setParameter("topic", topic)
                .getSingleResult();
    }

    @Override
    public List<Article> getAllNoContent() {
        log.info("Getting list of Article without field: content");
        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Article> criteriaQuery = builder.createQuery(Article.class);
        Root<Article> root = criteriaQuery.from(Article.class);

        criteriaQuery.multiselect(root.get("id"), root.get("authorName").alias("author_name"),
                root.get("authorSurname").alias("author_surname"),
                root.get("topic").alias("topic"),
                root.get("description").alias("description"),
                root.get("pictureUrl").alias("picture_url"),
                root.get("creationDate").alias("creation_date"));

        return session.createQuery(criteriaQuery).getResultList();
    }

}
