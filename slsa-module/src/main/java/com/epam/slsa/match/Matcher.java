package com.epam.slsa.match;

import com.epam.slsa.dto.criteria.CriteriaDto;
import com.epam.slsa.model.Location;

import java.util.List;

public interface Matcher {

    List<Location> getSuitableVenue(CriteriaDto criteria);

}

