package com.epam.spsa.mapper;

import com.epam.spsa.dto.article.ArticleDto;
import com.epam.spsa.model.Article;
import lombok.RequiredArgsConstructor;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TagMapper {

    public PropertyMap<ArticleDto, Article> toTag = new PropertyMap<ArticleDto, Article>() {
        @Override
        protected void configure() {
            skip(destination.getTags());
        }
    };

}
