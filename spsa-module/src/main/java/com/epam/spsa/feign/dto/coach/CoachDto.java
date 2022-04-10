package com.epam.spsa.feign.dto.coach;

import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.feign.dto.link.LinkDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachDto {

    private double rating;
    private boolean workWithChildren;
    private Integer userId;
    private Set<SportTypeDto> sportTypes;
    private Set<LinkDto> links;

}
