package com.epam.slsa.dao;

import com.epam.slsa.model.LocationType;

import java.util.List;

public interface LocationTypeDao extends CrudDao<LocationType> {

    List<LocationType> getAll();

}
