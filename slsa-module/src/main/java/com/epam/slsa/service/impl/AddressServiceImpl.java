package com.epam.slsa.service.impl;

import com.epam.slsa.dao.AddressDao;
import com.epam.slsa.dto.address.AddressDto;
import com.epam.slsa.dto.address.DetailedAddressDto;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.model.Address;
import com.epam.slsa.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressDao addressDao;

    private final ModelMapper mapper;

    @Value("${location.exception.notfound}")
    private String locationExceptionMessage;

    @Value("${address.exception.notfound}")
    private String addressExceptionMessage;

    @Value("${address.coordinates.exception.notfound}")
    private String addressCoordinatesNotFoundMessage;

    @Override
    public AddressDto save(AddressDto addressDto) {
        throw new UnsupportedOperationException("Saving an address separately from a location is not supported");
    }

    @Override
    public AddressDto update(AddressDto addressDto, int locationId) {
        throw new UnsupportedOperationException("Updating an address separately from a location is not supported");
    }

    @Override
    public AddressDto getById(int id) {
        log.info("Getting AddressDto by id: {}", id);
        Address address = addressDao.getById(id);
        if (address == null) {
            log.error("Address wasn't found. id: {}", id);
            throw new EntityNotFoundException(addressExceptionMessage + id);
        }
        return mapper.map(addressDao.getById(id), AddressDto.class);
    }

    @Override
    public AddressDto getByLocationId(int id) {
        log.info("Getting AddressDto by LocationId: {}", id);
        return mapper.map(getAddressByLocationId(id), AddressDto.class);
    }

    @Override
    public List<AddressDto> getAll() {
        log.info("Getting List of AddressDto");
        return addressDao
                .getAll()
                .stream()
                .map(a -> mapper.map(a, AddressDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public DetailedAddressDto getByCoordinates(double latitude, double longitude) {
        log.info("Getting address by latitude: {} and longitude: {}", latitude, longitude);
        try {
            return mapper.map(addressDao.getByCoordinates(latitude, longitude), DetailedAddressDto.class);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(addressCoordinatesNotFoundMessage);
        }
    }

    private Address getAddressByLocationId(int locationId) {
        Address address;
        try {
            address = addressDao.getByLocationId(locationId);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(locationExceptionMessage + locationId
                    + " or there is no address associated with that location id");
        }
        return address;
    }

}
