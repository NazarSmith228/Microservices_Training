package com.epam.slsa.match.impl;

import com.epam.slsa.dto.coach.CriteriaCoachDto;
import com.epam.slsa.dto.coachCriteria.CoachCriteriaDto;
import com.epam.slsa.feign.PartnerClient;
import com.epam.slsa.match.CoachMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoachMatcherImpl implements CoachMatcher {

    private final CoachCriteriaFilterImpl coachCriteriaFilter;

    private final PartnerClient partnerClient;

    private final ModelMapper mapper;

    @Override
    public List<CriteriaCoachDto> getSuitableCoach(List<CriteriaCoachDto> coaches, CoachCriteriaDto criteria) {
        log.info("Getting suitable coaches by Criteria: {}", criteria);

        if (criteria.getRating() != 0) {
            coaches = coachCriteriaFilter.getFilteredCoachesByRating(coaches, criteria);
        }

        if (criteria.getWorkWithChildren() != null) {
            coaches = coachCriteriaFilter.getFilteredCoachesByWorkWithChildren(coaches, criteria);
        }

        if (criteria.getSportType() != null) {
            coaches = coachCriteriaFilter.getFilteredCoachesBySportType(coaches, criteria);
        }

        coaches.forEach(c -> c.setUser(partnerClient.getUserById(c.getUserId())));

        if (criteria.getGender() != null) {
            coaches = coachCriteriaFilter.getFilteredCoachesByGender(coaches, criteria);
        }

        if (criteria.getLocationName() != null) {
            coaches = coachCriteriaFilter.getFilteredCoachesByLocation(coaches, criteria, partnerClient.getUserById(criteria.getUserId()));
        }

        return coaches;
    }
}
