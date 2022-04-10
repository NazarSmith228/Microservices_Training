package com.epam.spsa.service;

import com.epam.spsa.dto.sport.SportTypeDto;

import java.util.List;

public interface SportTypeService {

    SportTypeDto getSportTypeById(int id);

    List<SportTypeDto> getAllSportTypes();

}
