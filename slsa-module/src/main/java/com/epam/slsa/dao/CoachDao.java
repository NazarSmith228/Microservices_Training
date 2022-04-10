package com.epam.slsa.dao;

import com.epam.slsa.model.Coach;

import java.util.List;

public interface CoachDao extends CrudDao<Coach> {

    Coach getByIdAndLocationId(int coachId, int locationId);

    List<Coach> getAllByLocationId(int id);

    Coach getByUserId(int userId);

    List<Coach> getAll();

}
