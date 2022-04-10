package com.epam.spsa.dao;

import com.epam.spsa.model.Article;

import java.util.List;

public interface ArticleDao extends MainDao<Article> {

    void delete(Article article);

    Article update(Article article);

    Article getArticleByTopic(String topic);

    List<Article> getAllNoContent();

}
