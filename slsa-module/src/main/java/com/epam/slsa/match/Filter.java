package com.epam.slsa.match;

import com.epam.slsa.dto.criteria.CriteriaDto;
import com.epam.slsa.model.Location;
import com.epam.slsa.model.Placing;

import java.util.List;

public interface Filter {

    List<Location> getFilteredLocationsBySportType(List<Location> criteriaList, CriteriaDto criteria);

    List<Location> getFilteredLocationsByPlacing(List<Location> criteriaList, Placing placing);

    List<Location> getFilteredLocationsByAddress(List<Location> criteriaList, CriteriaDto criteria);

}
