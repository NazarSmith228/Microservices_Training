package com.epam.spsa.validation.validators.commonUserStatDto;

import com.epam.spsa.feign.SlsaClient;
import com.epam.spsa.feign.dto.location.LocationDto;
import com.epam.spsa.feign.dto.location.MainLocationDto;
import com.epam.spsa.validation.commonUserStatDto.LocationIdSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class LocationIdSetValidator implements ConstraintValidator<LocationIdSet, Set<Integer>> {

    private final SlsaClient locationClient;

    @Override
    public boolean isValid(Set<Integer> integers, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validation location ids {}", integers);
        if (integers == null) {
            return true;
        }

        List<MainLocationDto> locationDtoList = locationClient.getAll();
        Set<Integer> locationIds = locationDtoList.stream()
                .map(MainLocationDto::getId)
                .collect(Collectors.toSet());


        return locationIds.containsAll(integers);
    }

}
