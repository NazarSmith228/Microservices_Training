package com.epam.spsa.service.impl;

import com.epam.spsa.dao.AddressDao;
import com.epam.spsa.dto.address.AddressDto;
import com.epam.spsa.dto.address.DetailedAddressDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.Address;
import com.epam.spsa.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/exceptionMessages.properties")
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressDao addressDao;

    private final ModelMapper mapper;

    @Value("${address.coordinates.exception.notfound}")
    private String coordinatesExceptionMessage;

    @Value("${user.exception.notfound}")
    private String userExceptionMessage;

    @Override
    public List<AddressDto> getAllAddresses() {
        log.info("Getting List of AddressDto");
        return addressDao
                .getAll()
                .stream()
                .map(a -> mapper.map(a, AddressDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailedAddressDto> getAddressByCoordinates(double latitude, double longitude) {
        log.info("Getting Address(es) by coordinates: latitude: {}, longitude: {}", latitude, longitude);
        List<Address> addresses = addressDao.getByCoordinates(latitude, longitude);
        if (addresses.size() == 0) {
            throw new EntityNotFoundException(coordinatesExceptionMessage);
        }
        return addresses
                .stream()
                .map(x -> mapper.map(x, DetailedAddressDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto getAddressByUserId(int id) {
        log.info("Getting Address by User id: {}", id);
        try {
            return mapper.map(addressDao.getByUserId(id), AddressDto.class);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(userExceptionMessage + id);
        }
    }

}