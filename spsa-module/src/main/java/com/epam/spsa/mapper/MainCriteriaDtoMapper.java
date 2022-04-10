package com.epam.spsa.mapper;

import com.epam.spsa.dto.criteria.MainCriteriaDto;
import com.epam.spsa.model.Criteria;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class MainCriteriaDtoMapper {

    public PropertyMap<Criteria, MainCriteriaDto> toDto = new PropertyMap<Criteria, MainCriteriaDto>() {
        @Override
        protected void configure() {
            map(source.getUser(), destination.getUser());
        }
    };

}
