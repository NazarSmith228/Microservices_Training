package com.epam.slsa.match;

import com.epam.slsa.dto.coach.CriteriaCoachDto;
import com.epam.slsa.dto.coachCriteria.CoachCriteriaDto;
import com.epam.slsa.model.Coach;

import java.util.List;

public interface CoachMatcher {

    List<CriteriaCoachDto> getSuitableCoach(List<CriteriaCoachDto> coaches, CoachCriteriaDto criteria);

}
