package com.epam.spsa.service.impl;

import com.epam.spsa.dao.SportTypeDao;
import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.SportType;
import com.epam.spsa.service.SportTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PropertySource(value = "classpath:/exceptionMessages.properties")
@Slf4j
public class SportTypeServiceImpl implements SportTypeService {

    private final SportTypeDao sportTypeDao;

    private final ModelMapper mapper;

    @Value("${sportType.exception.notfound}")
    private String exceptionMessage;

    @Override
    public SportTypeDto getSportTypeById(int id) {
        log.info("Getting SportType by id: {}", id);
        SportType sportType = sportTypeDao.getById(id);
        if (sportType == null) {
            log.error("SportType wasn't found. id: {}", id);
            throw new EntityNotFoundException(exceptionMessage + id);
        }
        return mapper.map(sportType, SportTypeDto.class);
    }

    @Override
    public List<SportTypeDto> getAllSportTypes() {
        log.info("Getting List of SportTypeDto");
        return sportTypeDao
                .getAll()
                .stream()
                .map(st -> mapper.map(st, SportTypeDto.class))
                .collect(Collectors.toList());
    }

}