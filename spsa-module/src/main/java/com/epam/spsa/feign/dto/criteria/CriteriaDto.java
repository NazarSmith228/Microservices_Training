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
public class CriteriaDto {

    private Set<SportTypeDto> sportTypes;
    private double latitude;
    private double longitude;
    private String placing;

}
