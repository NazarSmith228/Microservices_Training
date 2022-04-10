package com.epam.slsa.dto.coach;

import com.epam.slsa.dto.comment.MainCommentDto;
import com.epam.slsa.dto.link.MainLinkDto;
import com.epam.slsa.dto.location.MainLocationDto;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.feign.dto.MainUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriteriaCoachDto {

    private int id;
    private double rating;
    private boolean workWithChildren;
    private int userId;
    private MainLocationDto location;
    private MainUserDto user;
    private Set<SportTypeDto> sportTypes;
    private Set<MainCommentDto> comments;
    private Set<MainLinkDto> links;

}
