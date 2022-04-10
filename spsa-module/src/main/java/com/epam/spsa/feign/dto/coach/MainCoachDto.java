package com.epam.spsa.feign.dto.coach;

import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.feign.dto.comment.MainCommentDto;
import com.epam.spsa.feign.dto.link.MainLinkDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainCoachDto {

    private int id;
    private double rating;
    private boolean workWithChildren;
    private int userId;
    private Set<SportTypeDto> sportTypes = new HashSet<>();
    private Set<MainCommentDto> comments = new HashSet<>();
    private Set<MainLinkDto> links = new HashSet<>();

}
