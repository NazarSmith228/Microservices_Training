package com.epam.slsa.match.impl;

import com.epam.slsa.dto.criteria.CriteriaDto;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.match.Filter;
import com.epam.slsa.model.Location;
import com.epam.slsa.model.Placing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilterImpl implements Filter {

    @Override
    public List<Location> getFilteredLocationsBySportType(List<Location> locationList, CriteriaDto criteria) {
        List<String> sportTypes = criteria.getSportTypes().stream()
                .map(SportTypeDto::getName).collect(Collectors.toList());
        log.info("Filtering List of Criteria by SportType: {}", sportTypes);

        return locationList.stream()
                .filter(
                        location -> location.getSportTypes().stream()
                                .anyMatch(sportType -> sportTypes.contains(sportType.getName())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Location> getFilteredLocationsByPlacing(List<Location> locationList, Placing placing) {
        log.info("Filtering List of Criteria by Placing: {}", placing.getName());
        if (placing.equals(Placing.ANY)) {
            return locationList;
        } else {
            return locationList.stream()
                    .filter(location -> {
                        Placing currentPlacing = location.getLocationType().getPlacing(); //!!!
                        if (currentPlacing.equals(Placing.ANY)) {
                            return true;
                        } else {
                            return currentPlacing.equals(placing);
                        }
                    })
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Location> getFilteredLocationsByAddress(List<Location> locationList, CriteriaDto criteria) {
        log.info("Filtering List of Criteria by Address: {}, {}", criteria.getLatitude(), criteria.getLongitude());
        int distance = 20;
        return locationList.stream()
                .filter(location ->
                        calculateDistanceBetweenTwoLocations(
                                location.getAddress().getLatitude(),
                                location.getAddress().getLongitude(),
                                criteria.getLatitude(), criteria.getLongitude()) < distance)
                .collect(Collectors.toList());
    }

    private double calculateDistanceBetweenTwoLocations(double latitudeStart, double longitudeStart, double latitudeEnd, double longitudeEnd) {
        log.info("Calculating distance between two locations. latitudeStart: {}, longitudeStart: {}, ",
                latitudeStart, longitudeStart);

        log.debug("\tlatitudeEnd: {}, longitudeEnd: {}", latitudeEnd, longitudeEnd);

        double longitudeDifference = longitudeStart - longitudeEnd;
        log.debug("Longitude Difference: {}", longitudeDifference);

        double distance = Math.sin(Math.toRadians(latitudeStart)) * Math.sin(Math.toRadians(latitudeEnd))
                + Math.cos(Math.toRadians(latitudeStart)) * Math.cos(Math.toRadians(latitudeEnd))
                * Math.cos(Math.toRadians(longitudeDifference));
        log.debug("distance: {}", distance);
        double multiplier = 111.189;
        return Math.toDegrees(Math.acos(distance)) * multiplier;
    }

}
