package com.epam.spsa.feign.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainCommentDto {

    private int id;
    private int userId;
    private int coachId;
    private String creationDate;
    private String comment;
    private int rating;

}