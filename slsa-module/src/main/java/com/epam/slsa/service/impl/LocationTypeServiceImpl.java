package com.epam.slsa.service.impl;

import com.epam.slsa.dao.LocationTypeDao;
import com.epam.slsa.dto.locationType.LocationTypeDto;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.model.LocationType;
import com.epam.slsa.service.LocationTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class LocationTypeServiceImpl implements LocationTypeService {

    private final LocationTypeDao locationTypeDao;

    private final ModelMapper mapper;

    @Value("${locationType.exception.notfound}")
    private String locationTypeNotFound;

    @Override
    public LocationTypeDto getById(int id) {
        log.info("Getting LocationTypeDto by id: {}", id);
        LocationType locationType = locationTypeDao.getById(id);
        if (locationType == null) {
            log.error("LocationType wasn't found. id: {}", id);
            throw new EntityNotFoundException(locationTypeNotFound + id);
        }
        return mapper.map(locationType, LocationTypeDto.class);
    }

    @Override
    public List<LocationTypeDto> getAll() {
        log.info("Getting List of LocationTypeDto");
        List<LocationType> all = locationTypeDao.getAll();
        return all
                .stream()
                .map(lt -> mapper.map(lt, LocationTypeDto.class))
                .collect(Collectors.toList());
    }

}
