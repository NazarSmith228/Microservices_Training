package com.epam.slsa.validation.validators;

import com.epam.slsa.dao.LocationTypeDao;
import com.epam.slsa.dto.locationType.LocationTypeDto;
import com.epam.slsa.model.LocationType;
import com.epam.slsa.validation.LocationTypeSubset;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@RequiredArgsConstructor
public class LocationTypeValidator implements ConstraintValidator<LocationTypeSubset, LocationTypeDto> {

    private final LocationTypeDao locationTypeDao;

    private final ModelMapper mapper;

    @Override
    public boolean isValid(LocationTypeDto value, ConstraintValidatorContext context) {
        List<LocationType> allLocation = locationTypeDao.getAll();
        return allLocation.contains(mapper.map(value, LocationType.class));
    }

}

