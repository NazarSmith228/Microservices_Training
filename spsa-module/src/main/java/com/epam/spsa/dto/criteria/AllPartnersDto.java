package com.epam.spsa.dto.criteria;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllPartnersDto {

    private List<CriteriaUserDto> partners;
    private long partnersQuantity;
    private long partnersLeft;

}
