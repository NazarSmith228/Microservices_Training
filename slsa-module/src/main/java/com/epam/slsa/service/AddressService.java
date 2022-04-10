package com.epam.slsa.service;

import com.epam.slsa.dto.address.AddressDto;
import com.epam.slsa.dto.address.DetailedAddressDto;

import java.util.List;

public interface AddressService {

    AddressDto save(AddressDto addressDto);

    AddressDto update(AddressDto addressDto, int locationId);

    AddressDto getById(int id);

    AddressDto getByLocationId(int id);

    List<AddressDto> getAll();

    DetailedAddressDto getByCoordinates(double latitude, double longitude);

}
