package com.epam.slsa.dao;

import com.epam.slsa.model.LocationSchedule;

import java.util.List;

public interface LocationScheduleDao extends CrudDao<LocationSchedule> {

    List<LocationSchedule> getAllByLocationId(int locationId);

}
