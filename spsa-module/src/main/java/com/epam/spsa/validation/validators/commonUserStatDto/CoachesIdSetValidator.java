package com.epam.spsa.validation.validators.commonUserStatDto;

import com.epam.spsa.feign.SlsaClient;
import com.epam.spsa.feign.dto.coach.MainCoachDto;
import com.epam.spsa.feign.dto.location.MainLocationDto;
import com.epam.spsa.validation.commonUserStatDto.CoachesIdSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class CoachesIdSetValidator implements ConstraintValidator<CoachesIdSet, Set<Integer>> {

    private final SlsaClient locationClient;

    @Override
    public boolean isValid(Set<Integer> integers, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validation set of coaches id {}", integers);
        if (integers == null) {
            return true;
        }

        List<MainLocationDto> listOfLocation = locationClient.getAll();
        Set<Integer> listIdOfCoaches = listOfLocation.stream()
                .flatMap(mainLocationDto -> mainLocationDto
                        .getCoaches()
                        .stream()
                        .map(MainCoachDto::getId)).collect(Collectors.toSet());

        log.info("Ids of coaches in db {}", listIdOfCoaches);

        return listIdOfCoaches.containsAll(integers);
    }

}
