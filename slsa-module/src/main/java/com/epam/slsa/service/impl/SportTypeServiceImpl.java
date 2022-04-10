package com.epam.slsa.service.impl;

import com.epam.slsa.dao.SportTypeDao;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.model.SportType;
import com.epam.slsa.service.SportTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class SportTypeServiceImpl implements SportTypeService {

    private final SportTypeDao sportTypeDao;

    private final ModelMapper mapper;

    @Value("${sportType.exception.notfound}")
    private String sportTypeNotFound;

    @Override
    public SportTypeDto getById(int id) {
        log.info("Getting SportTypeDto by id: {}", id);
        SportType sportType = sportTypeDao.getById(id);
        if (sportType == null) {
            log.error("SportType wasn't found. id: {}", id);
            throw new EntityNotFoundException(sportTypeNotFound + id);
        }
        return mapper.map(sportType, SportTypeDto.class);
    }

    @Override
    public List<SportTypeDto> getAll() {
        log.info("Getting List of SportTypeDto");
        return sportTypeDao
                .getAll()
                .stream()
                .map(st -> mapper.map(st, SportTypeDto.class))
                .collect(Collectors.toList());
    }

}
