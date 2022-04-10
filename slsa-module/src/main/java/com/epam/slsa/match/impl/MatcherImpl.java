package com.epam.slsa.match.impl;

import com.epam.slsa.dao.LocationDao;
import com.epam.slsa.dto.criteria.CriteriaDto;
import com.epam.slsa.match.Matcher;
import com.epam.slsa.model.Location;
import com.epam.slsa.model.Placing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatcherImpl implements Matcher {

    private final LocationDao locationDao;
    private final FilterImpl filter;

    @Override
    public List<Location> getSuitableVenue(CriteriaDto criteria) {
        log.info("Getting suitable partner by Criteria: {}", criteria);
        List<Location> locationList = locationDao.getAll();
        if (criteria.getSportTypes() != null && !criteria.getSportTypes().isEmpty()) {
            locationList = filter.getFilteredLocationsBySportType(locationList, criteria);
            log.debug("Filtered by SportType");
        }
        if (criteria.getLongitude() != 0 && criteria.getLatitude() != 0) {
            locationList = filter.getFilteredLocationsByAddress(locationList, criteria);
            log.debug("Filtered by Address");
        }
        if (criteria.getPlacing() != null) {
            locationList = filter.getFilteredLocationsByPlacing(locationList, Placing.getFromName(criteria.getPlacing()));
            log.debug("Filtered by Placing");
        }

        return locationList;
    }

}
