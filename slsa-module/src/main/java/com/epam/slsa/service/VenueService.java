package com.epam.slsa.service;

import com.epam.slsa.dto.criteria.CriteriaDto;
import com.epam.slsa.dto.location.CriteriaLocationDto;

import java.util.List;

public interface VenueService {

    List<CriteriaLocationDto> getSuitableLocation(CriteriaDto criteriaDto);

}
