package com.epam.slsa.mapper;

import com.epam.slsa.dto.coach.CriteriaCoachDto;
import com.epam.slsa.model.Coach;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class CriteriaCoachDtoMapper extends PropertyMap<Coach, CriteriaCoachDto> {

    @Override
    protected void configure() {
        skip(destination.getLocation());
    }

}
