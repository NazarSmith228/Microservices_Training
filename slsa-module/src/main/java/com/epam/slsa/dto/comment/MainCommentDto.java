package com.epam.slsa.dto.comment;

import com.epam.slsa.feign.dto.MainUserDto;
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
    private MainUserDto user;
    private int coachId;
    private String creationDate;
    private String comment;
    private int rating;

}
