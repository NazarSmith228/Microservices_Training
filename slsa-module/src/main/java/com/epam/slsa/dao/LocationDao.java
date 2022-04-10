package com.epam.slsa.dao;

import com.epam.slsa.model.Location;

import java.util.List;

public interface LocationDao extends CrudDao<Location> {

    List<Location> getByName(String name);

    List<Location> getByAdminId(int adminId);

    Location getByPhoneNumber(String phoneNumber);

    List<Location> getAll();

}
