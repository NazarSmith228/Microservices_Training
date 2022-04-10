package com.epam.spsa.validation.validators.commonUserStatDto;

import com.epam.spsa.dao.SportTypeDao;

import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.model.SportType;
import com.epam.spsa.validation.commonUserStatDto.SportTypesSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SportTypesSetValidator implements ConstraintValidator<SportTypesSet, Set<SportTypeDto>> {

    private final SportTypeDao sportTypeDao;

    private final ModelMapper modelMapper;

    @Override
    public boolean isValid(Set<SportTypeDto> sportTypeDtos, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validation set of sport types {}", sportTypeDtos);
        if (sportTypeDtos == null) {
            return true;
        }
        List<SportType> list = sportTypeDao.getAll();
        List<SportType> currentList = sportTypeDtos.stream()
                .map(sportTypeDto -> modelMapper.map(sportTypeDto, SportType.class))
                .collect(Collectors.toList());

        return list.containsAll(currentList);
    }

}
