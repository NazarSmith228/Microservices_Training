package com.epam.spsa.controller.builder;

import com.epam.spsa.model.Estimation;

public class EstimationBuilder {

    public static Estimation getLike() {
        return Estimation.builder()
                .id(1)
                .comment(ArticleCommentBuilder.getArticleComment(1))
                .user(UserBuilder.getUser())
                .value(1)
                .build();
    }

    public static Estimation getDislike() {
        return Estimation.builder()
                .id(2)
                .comment(ArticleCommentBuilder.getArticleComment(1))
                .user(UserBuilder.getUser())
                .value(-1)
                .build();
    }

}
