package com.epam.slsa.mapper;

import com.epam.slsa.dto.comment.MainCommentDto;
import com.epam.slsa.model.Comment;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class CommentDtoMapper {

    public PropertyMap<Comment, MainCommentDto> toDto = new PropertyMap<Comment, MainCommentDto>() {
        @Override
        protected void configure() {
            map(source.getCoach().getId(), destination.getCoachId());
        }
    };

}