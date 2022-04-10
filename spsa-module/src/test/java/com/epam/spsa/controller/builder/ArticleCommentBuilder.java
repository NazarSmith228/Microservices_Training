package com.epam.spsa.controller.builder;

import com.epam.spsa.dto.article.ArticleCommentDto;
import com.epam.spsa.dto.article.MainArticleCommentDto;
import com.epam.spsa.model.ArticleComment;

import java.sql.Timestamp;

public class ArticleCommentBuilder {

    public static MainArticleCommentDto getMainArticleCommentDto(int id) {
        return MainArticleCommentDto.builder()
                .id(id)
                .creationDate(new Timestamp(System.currentTimeMillis()))
                .content("My content")
                .image("https:..")
                .likes(3)
                .dislikes(2)
                .build();
    }
     public static ArticleComment getArticleComment(int id) {
        return ArticleComment.builder()
                .id(id)
                .creationDate(new Timestamp(System.currentTimeMillis()))
                .content("My content")
                .image("https:..")
                .user(UserBuilder.getUser())
                .estimations(null)
                .build();
    }

    public static ArticleCommentDto getArticleCommentDto(int id) {
        return ArticleCommentDto.builder()
                .content("My content")
                .build();
    }

}
