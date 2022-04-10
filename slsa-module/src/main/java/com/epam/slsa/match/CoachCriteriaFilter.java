package com.epam.slsa.match;

import com.epam.slsa.dto.coach.CriteriaCoachDto;
import com.epam.slsa.dto.coachCriteria.CoachCriteriaDto;
import com.epam.slsa.feign.dto.MainUserDto;

import java.util.List;

public interface CoachCriteriaFilter {

    List<CriteriaCoachDto> getFilteredCoachesByGender(List<CriteriaCoachDto> coaches, CoachCriteriaDto criteria);

    List<CriteriaCoachDto> getFilteredCoachesBySportType(List<CriteriaCoachDto> coaches, CoachCriteriaDto criteria);

    List<CriteriaCoachDto> getFilteredCoachesByWorkWithChildren(List<CriteriaCoachDto> coaches, CoachCriteriaDto criteria);

    List<CriteriaCoachDto> getFilteredCoachesByLocation(List<CriteriaCoachDto> coaches, CoachCriteriaDto criteria, MainUserDto user);

    List<CriteriaCoachDto> getFilteredCoachesByRating(List<CriteriaCoachDto> coaches, CoachCriteriaDto criteria);

}
