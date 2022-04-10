package com.epam.spsa.mapper;

import com.epam.spsa.dto.article.MainArticleCommentDto;
import com.epam.spsa.model.ArticleComment;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class MainArticleCommentDtoMapper {

    public PropertyMap<ArticleComment, MainArticleCommentDto> toMainDto = new PropertyMap<ArticleComment, MainArticleCommentDto>() {

        @Override
        protected void configure() {
            map(source.getLikes(), destination.getLikes());
            map(source.getDislikes(), destination.getDislikes());
        }
    };

}
