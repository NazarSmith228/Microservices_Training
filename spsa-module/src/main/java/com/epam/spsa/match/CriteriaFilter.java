package com.epam.spsa.match;

import com.epam.spsa.dto.criteria.CriteriaDto;
import com.epam.spsa.dto.criteria.CriteriaUserDto;
import com.epam.spsa.model.ActivityTime;
import com.epam.spsa.model.Address;
import com.epam.spsa.model.Criteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@PropertySource(value = "classpath:/messages.properties")
public class CriteriaFilter {

    private final ModelMapper modelMapper;

    public List<Criteria> removeMainUserFromList(List<Criteria> criteriaList, int id) {
        log.info("Removing Main User from List of Criteria");
        log.info("User id: {}", id);
        return criteriaList.stream()
                .filter(criteria -> criteria.getUser().getId() != id)
                .collect(Collectors.toList());
    }

    public List<Criteria> getFilteredUsersByGender(List<Criteria> criteriaList, Criteria mainCriteria) {

        String criteriaGender = mainCriteria.getGender().getGender();
        String userGender = mainCriteria.getUser().getGender().getGender();

        log.info("Filtering List of Criteria by Gender: {}", criteriaGender);
        return criteriaList.stream()
                .filter(criteria -> GenderFilter.checkIfNotNull(criteria) && (
                        GenderFilter.checkFirstGenderCase(userGender, criteriaGender, criteria)
                                || GenderFilter.checkSecondGenderCase(userGender, criteriaGender, criteria)
                                || GenderFilter.checkThirdGenderCase(criteriaGender, criteria)
                                || GenderFilter.checkFourthGenderCase(criteriaGender, criteria)))
                .collect(Collectors.toList());
    }

    public List<Criteria> getFilteredUsersByDayTime(List<Criteria> criteriaList, String dayTime) {
        log.info("Filtering List of Criteria by ByDayTime: {}", dayTime);
        return criteriaList.stream()
                .filter(criteria -> criteria.getActivityTime() != null &&
                        (criteria.getActivityTime().getDayPart().equalsIgnoreCase(dayTime)
                                || criteria.getActivityTime().getDayPart().equals(ActivityTime.ALL.getDayPart())
                                || dayTime.equalsIgnoreCase(ActivityTime.ALL.getDayPart())))
                .collect(Collectors.toList());
    }

    public List<Criteria> getFilteredUsersBySportType(List<Criteria> criteriaList, Criteria mainCriteria) {
        String sportType = mainCriteria.getSportType().getName();
        log.info("Filtering List of Criteria by SportType: {}", sportType);

        if (sportType.equalsIgnoreCase("Running")) {
            double distance = mainCriteria.getRunningDistance();
            log.debug("Running distance: {}", distance);

            int[] interval = getInterval(distance);
            return criteriaList.stream()
                    .filter(criteria -> criteria.getSportType() != null &&
                            criteria.getSportType().getName().equalsIgnoreCase(sportType)
                            && criteria.getRunningDistance() >= interval[0]
                            && criteria.getRunningDistance() <= interval[1])
                    .collect(Collectors.toList());
        }
        return criteriaList.stream()
                .filter(criteria -> criteria.getSportType().getName().equalsIgnoreCase(sportType))
                .collect(Collectors.toList());
    }

    private int[] getInterval(double distance) {
        log.info("Getting interval by distance: {}", distance);

        int[] interval = new int[2];
        if (distance <= 2) {
            interval[1] = 2;
        } else if (distance > 2 && distance <= 5) {
            interval[0] = 2;
            interval[1] = 5;
        } else if (distance > 5 && distance <= 10) {
            interval[0] = 5;
            interval[1] = 10;
        } else if (distance > 10 && distance <= 50) {
            interval[0] = 10;
            interval[1] = 50;
        }

        log.info("Result interval: {}-{}", interval[0], interval[1]);
        return interval;
    }

    public List<Criteria> getFilteredUsersByMaturity(List<Criteria> criteriaList, String maturity) {
        log.info("Filtering List of Criteria by Maturity: {}", maturity);
        return criteriaList.stream()
                .filter(criteria -> criteria.getMaturity() != null &&
                        criteria.getMaturity().getMaturity().equalsIgnoreCase(maturity))
                .collect(Collectors.toList());
    }

    public List<Criteria> getFilteredUsersByLocation(List<Criteria> criteriaList, Address address) {
        log.info("Filtering List of Criteria by Address with id: {}", address.getId());
        int distance = 20;
        return criteriaList.stream()
                .filter(criteria ->
                        calculateDistanceBetweenTwoUsers(
                                criteria.getUser().getAddress().getLatitude(),
                                criteria.getUser().getAddress().getLongitude(),
                                address.getLatitude(), address.getLongitude()) < distance)
                .collect(Collectors.toList());
    }

    private double calculateDistanceBetweenTwoUsers(double latitudeStart, double longitudeStart,
                                                    double latitudeEnd, double longitudeEnd) {
        log.info("Calculating distance between two users. latitudeStart: {}, longitudeStart: {}, ",
                latitudeStart, longitudeStart);
        double multiplier = 111.189;

        log.debug("\tlatitudeEnd: {}, longitudeEnd: {}", latitudeEnd, longitudeEnd);

        double longitudeDifference = longitudeStart - longitudeEnd;
        log.debug("Longitude Difference: {}", longitudeDifference);

        double distance = Math.sin(Math.toRadians(latitudeStart)) * Math.sin(Math.toRadians(latitudeEnd))
                + Math.cos(Math.toRadians(latitudeStart)) * Math.cos(Math.toRadians(latitudeEnd))
                * Math.cos(Math.toRadians(longitudeDifference));
        log.debug("distance: {}", distance);

        return Math.toDegrees(Math.acos(distance)) * multiplier;
    }

    public List<CriteriaUserDto> getUsers(List<Criteria> criteriaList, int pageNumber, int pageSize) {
        log.info("Getting users from criteria list: pageNumber - {}, pageSize - {}", pageNumber, pageSize);
        Collections.reverse(criteriaList);
        List<CriteriaUserDto> userList = new ArrayList<>();
        for (Criteria criteria : criteriaList) {
            CriteriaUserDto criteriaUserDto = modelMapper.map(criteria.getUser(), CriteriaUserDto.class);
            if (!userList.contains(criteriaUserDto)) {
                criteriaUserDto.setCriteriaDto(modelMapper.map(criteria, CriteriaDto.class));
                userList.add(criteriaUserDto);
            }
        }

        return userList;
    }

}
