package com.epam.slsa.converter;

import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.model.SportType;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SportTypeListConverter implements Converter<Set<SportTypeDto>, Set<SportType>> {

    @Override
    public Set<SportType> convert(MappingContext<Set<SportTypeDto>, Set<SportType>> context) {
        if (context.getSource() != null) {
            return context.getSource()
                    .stream()
                    .map(sp ->
                            SportType
                                    .builder()
                                    .id(sp.getId())
                                    .name(sp.getName())
                                    .build()
                    ).collect(Collectors.toSet());
        }
        return null;
    }

}
