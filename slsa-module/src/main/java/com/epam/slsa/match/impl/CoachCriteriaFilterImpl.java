package com.epam.slsa.match.impl;

import com.epam.slsa.dto.address.AddressDto;
import com.epam.slsa.dto.coach.CriteriaCoachDto;
import com.epam.slsa.dto.coachCriteria.CoachCriteriaDto;
import com.epam.slsa.feign.dto.MainUserDto;
import com.epam.slsa.match.CoachCriteriaFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class CoachCriteriaFilterImpl implements CoachCriteriaFilter {

    @Override
    public List<CriteriaCoachDto> getFilteredCoachesByRating(List<CriteriaCoachDto> coaches, CoachCriteriaDto criteria) {
        double rating = criteria.getRating();
        log.info("Filtering List of Coaches by Rating: {}", rating);

        return coaches.stream()
                .filter(c -> c.getRating() >= rating && c.getRating() < rating + 1)
                .collect(Collectors.toList());
    }

    @Override
    public List<CriteriaCoachDto> getFilteredCoachesByWorkWithChildren(List<CriteriaCoachDto> coaches, CoachCriteriaDto criteria) {
        Boolean workWithChildren = criteria.getWorkWithChildren();
        log.info("Filtering List of Coaches by WorkWithChildren: {}", workWithChildren);

        return coaches.stream()
                .filter(c -> c.isWorkWithChildren() == workWithChildren)
                .collect(Collectors.toList());
    }

    @Override
    public List<CriteriaCoachDto> getFilteredCoachesByGender(List<CriteriaCoachDto> coaches, CoachCriteriaDto criteria) {
        String gender = criteria.getGender();
        log.info("Filtering List of Coaches by Gender: {}", gender);
        return coaches.stream()
                .filter(c -> (c.getUser().getGender().equalsIgnoreCase(gender)))
                .collect(Collectors.toList());
    }

    @Override
    public List<CriteriaCoachDto> getFilteredCoachesBySportType(List<CriteriaCoachDto> coaches, CoachCriteriaDto criteria) {
        String sportType = criteria.getSportType();
        log.info("Filtering List of Coaches by SportType: {}", sportType);

        return coaches.stream()
                .filter(c -> c.getSportTypes().stream()
                        .anyMatch(s -> s.getName().equals(sportType)))
                .collect(Collectors.toList());
    }

    @Override
    public List<CriteriaCoachDto> getFilteredCoachesByLocation(List<CriteriaCoachDto> coaches, CoachCriteriaDto criteria, MainUserDto user) {
        String location = criteria.getLocationName();
        AddressDto userAddress = user.getAddress();

        log.info("Filtering List of Coaches by Location: {}", location);

        int distance = 5;

        return coaches.stream()
                .filter(c -> (c.getLocation().getName().equalsIgnoreCase(location)) &&
                        (calculateDistanceBetweenUserAndLocation(
                                userAddress.getLatitude(),
                                userAddress.getLongitude(),
                                c.getLocation().getAddress().getLatitude(),
                                c.getLocation().getAddress().getLongitude()) <= distance))
                .collect(Collectors.toList());

    }

    private double calculateDistanceBetweenUserAndLocation(double latitudeUser, double longitudeUser,
                                                           double latitudeLocation, double longitudeLocation) {
        log.info("Calculating distance between user and location. latitudeUser: {}, longitudeUser: {}, ",
                latitudeUser, longitudeUser);
        double multiplier = 111.189;

        log.info("\tlatitudeLocation: {}, longitudeLocation: {}", latitudeLocation, longitudeLocation);

        double longitudeDifference = longitudeUser - longitudeLocation;
        log.info("Longitude Difference: {}", longitudeDifference);

        double distance = Math.sin(Math.toRadians(latitudeUser)) * Math.sin(Math.toRadians(latitudeLocation))
                + Math.cos(Math.toRadians(latitudeUser)) * Math.cos(Math.toRadians(latitudeUser))
                * Math.cos(Math.toRadians(longitudeDifference));
        log.info("distance: {}", distance);
        return Math.toDegrees(Math.acos(distance)) * multiplier;
    }

}
