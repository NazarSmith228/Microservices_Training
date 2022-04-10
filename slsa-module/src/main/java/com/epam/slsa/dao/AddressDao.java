package com.epam.slsa.dao;

import com.epam.slsa.model.Address;

import java.util.List;

public interface AddressDao extends CrudDao<Address> {

    Address getByLocationId(int locationId);

    Address getByCoordinates(double latitude, double longitude);

    List<Address> getAll();

}
