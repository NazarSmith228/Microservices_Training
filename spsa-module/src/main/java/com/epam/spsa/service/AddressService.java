package com.epam.spsa.service;

import com.epam.spsa.dto.address.AddressDto;
import com.epam.spsa.dto.address.DetailedAddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAllAddresses();

    List<DetailedAddressDto> getAddressByCoordinates(double latitude, double longitude);

    AddressDto getAddressByUserId(int id);

}
