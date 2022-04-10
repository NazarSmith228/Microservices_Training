package com.epam.spsa.feign.dto.criteria;

import com.epam.spsa.dto.sport.SportTypeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainCriteriaDto {

    private int id;
    private double latitude;
    private double longitude;
    private Set<SportTypeDto> sportTypes;
    private String placing;

}
