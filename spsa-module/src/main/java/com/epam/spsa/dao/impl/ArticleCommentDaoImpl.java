package com.epam.spsa.dao.impl;

import com.epam.spsa.dao.ArticleCommentDao;
import com.epam.spsa.model.ArticleComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Slf4j
public class ArticleCommentDaoImpl implements ArticleCommentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public ArticleComment save(ArticleComment articleComment) {
        log.info("Saving article comment: {}", articleComment);
        entityManager.persist(articleComment);
        return articleComment;
    }

    @Override
    public ArticleComment getById(int id) {
        log.info("Getting article comment by id: {}", id);
        return entityManager.find(ArticleComment.class, id);
    }

    @Override
    public List<ArticleComment> getAll() {
        log.info("Getting List of article comments");
        return entityManager.createQuery("SELECT c FROM ArticleComment c WHERE c.article is not null"
                , ArticleComment.class)
                .getResultList();
    }

    @Override
    public List<ArticleComment> getAllByArticleId(int id) {
        log.info("Getting List of article comments by article id: {}", id);
        return entityManager.createQuery("SELECT c FROM ArticleComment c WHERE c.article.id = :id"
                , ArticleComment.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<ArticleComment> getAllByArticleIdASC(int id) {
        log.info("Getting ASC List of article comments by article id: {}", id);
        return entityManager.createQuery("SELECT c FROM ArticleComment c WHERE c.article.id = :id " +
                        "ORDER BY c.creationDate ASC"
                , ArticleComment.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<ArticleComment> getAllByArticleIdDESK(int id) {
        log.info("Getting DESC List of article comments by article id: {}", id);
        return entityManager.createQuery("SELECT c FROM ArticleComment c WHERE c.article.id = :id " +
                        "ORDER BY c.creationDate DESC"
                , ArticleComment.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    @Transactional
    public void deleteComment(int id) {
        log.info("Deleting article comments by comment id: {}", id);
        ArticleComment articleComment = entityManager.find(ArticleComment.class, id);
        entityManager.remove(articleComment);
    }

    @Override
    @Transactional
    public ArticleComment update(ArticleComment articleComment) {
        log.info("Updating article comments by comment id: {}", articleComment.getId());
        return entityManager.merge(articleComment);
    }

    @Override
    public ArticleComment getDeletedComment() {
        List<ArticleComment> list = entityManager.createQuery("SELECT c FROM ArticleComment c WHERE c.user is null "
                , ArticleComment.class).getResultList();
        if (list.size() == 0) return null;
        return list.get(0);
    }

}
