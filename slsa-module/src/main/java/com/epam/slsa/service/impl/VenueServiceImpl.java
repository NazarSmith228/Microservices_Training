package com.epam.slsa.service.impl;

import com.epam.slsa.dto.criteria.CriteriaDto;
import com.epam.slsa.dto.location.CriteriaLocationDto;
import com.epam.slsa.feign.PartnerClient;
import com.epam.slsa.match.Matcher;
import com.epam.slsa.service.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VenueServiceImpl implements VenueService {

    private final Matcher matcher;

    private final ModelMapper modelMapper;

    private final PartnerClient partnerClient;

    @Value("{criteria.exception.notfound}")
    private String criteriaExceptionMessage;

    @Override
    public List<CriteriaLocationDto> getSuitableLocation(CriteriaDto criteriaDto) {
        log.info("Getting list of MainLocationDto by Criteria");
        log.debug("\tCriteriaDto: sport type={}", criteriaDto.getSportTypes());
        return matcher.getSuitableVenue(criteriaDto).stream()
                .map(location -> {
                    CriteriaLocationDto criteriaLocationDto = modelMapper.map(location, CriteriaLocationDto.class);
                    criteriaLocationDto
                            .getCoaches()
                            .forEach(c -> c.setUser(partnerClient.getUserById(c.getUserId())));
                    return criteriaLocationDto;
                }).collect(Collectors.toList());
    }

}
