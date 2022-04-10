package com.epam.slsa.validation.validators;

import com.epam.slsa.dao.SportTypeDao;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.model.SportType;
import com.epam.slsa.validation.SportTypeSubset;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class SportTypeSubsetValidator implements ConstraintValidator<SportTypeSubset, Set<SportTypeDto>> {

    private final SportTypeDao sportTypeDao;

    private final ModelMapper mapper;

    @Override
    public boolean isValid(Set<SportTypeDto> sportTypes, ConstraintValidatorContext constraintValidatorContext) {
        if (sportTypes != null && !sportTypes.isEmpty()) {
            List<SportType> allSportTypes = sportTypeDao.getAll();
            return allSportTypes.containsAll(mapper.map(sportTypes, new TypeToken<List<SportType>>() {
            }.getType()));
        }
        return true;
    }

}
