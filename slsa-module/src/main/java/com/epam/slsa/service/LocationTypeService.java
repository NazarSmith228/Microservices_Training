package com.epam.slsa.service;

import com.epam.slsa.dto.locationType.LocationTypeDto;

import java.util.List;

public interface LocationTypeService {

    LocationTypeDto getById(int id);

    List<LocationTypeDto> getAll();

}
