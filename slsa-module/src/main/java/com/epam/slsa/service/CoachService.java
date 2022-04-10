package com.epam.slsa.service;

import com.epam.slsa.dto.coach.CoachDto;
import com.epam.slsa.dto.coach.CriteriaCoachDto;
import com.epam.slsa.dto.coach.MainCoachDto;
import com.epam.slsa.dto.coachCriteria.CoachCriteriaDto;

import java.util.List;

public interface CoachService {

    MainCoachDto save(CoachDto coachDto, int locationId);

    MainCoachDto update(CoachDto editedCoachDto, int locationId, int coachId);

    void delete(int id);

    MainCoachDto getById(int id);

    List<MainCoachDto> getAll();

    MainCoachDto getByIdAndLocationId(int coachId, int locationId);

    List<MainCoachDto> getAllByLocationId(int id);

    MainCoachDto getByUserId(int userId);

    void setNewLocationByCoachId(int locationId, int coachId);

    void deleteCoachLinkById(int linkId);

    List<CriteriaCoachDto> getSuitableCoach(CoachCriteriaDto criteria);

}
