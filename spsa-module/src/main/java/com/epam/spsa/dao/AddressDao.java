package com.epam.spsa.dao;

import com.epam.spsa.model.Address;

import java.util.List;

public interface AddressDao extends MainDao<Address> {

    List<Address> getByCoordinates(double latitude, double longitude);

    Address getByUserId(int id);

}
