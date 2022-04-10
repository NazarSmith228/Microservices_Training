package com.epam.spsa.mapper;

import com.epam.spsa.dto.criteria.CriteriaDto;
import com.epam.spsa.model.Criteria;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class CriteriaDtoMapper {

    public PropertyMap<CriteriaDto, Criteria> toModel = new PropertyMap<CriteriaDto, Criteria>() {
        @Override
        protected void configure() {
            skip(destination.getUser());
        }
    };

}
