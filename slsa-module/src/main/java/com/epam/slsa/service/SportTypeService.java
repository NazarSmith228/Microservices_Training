package com.epam.slsa.service;

import com.epam.slsa.dto.sportType.SportTypeDto;

import java.util.List;

public interface SportTypeService {

    SportTypeDto getById(int id);

    List<SportTypeDto> getAll();

}
