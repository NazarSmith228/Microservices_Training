package com.epam.slsa.validation.validators;

import com.epam.slsa.dao.SportTypeDao;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.model.SportType;
import com.epam.slsa.validation.SportTypeValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SportTypeValidator implements ConstraintValidator<SportTypeValue, SportTypeDto> {

    private final SportTypeDao sportTypeDao;

    private final ModelMapper mapper;

    @Override
    public boolean isValid(SportTypeDto sportType, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validating SportType with value: {}", sportType);
        List<SportType> allSportTypes = sportTypeDao.getAll();
        return allSportTypes.contains(mapper.map(sportType, SportType.class));
    }

}

