package com.epam.spsa.dto.article;

import com.epam.spsa.dto.user.PartialUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainArticleCommentDto {

    private int id;
    private String content;
    private Timestamp creationDate;
    private String image;
    private PartialUserDto user;
    private List<MainArticleCommentDto> comments;
    private int likes;
    private int dislikes;

}
