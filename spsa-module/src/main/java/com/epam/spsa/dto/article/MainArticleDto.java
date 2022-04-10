package com.epam.spsa.dto.article;

import com.epam.spsa.dto.tag.MainTagDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainArticleDto {

    private int id;
    private String authorName;
    private String authorSurname;
    private String topic;
    private String description;
    private String pictureUrl;
    private LocalDate creationDate;
    private String content;
    private Set<MainTagDto> tags;

}
