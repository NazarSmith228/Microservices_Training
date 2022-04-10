package com.epam.spsa.converter;

import com.epam.spsa.model.User;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component

public class UserIdListConverter implements Converter<List<User>, List<Integer>> {

    @Override
    public List<Integer> convert(MappingContext<List<User>, List<Integer>> mappingContext) {
        return mappingContext.getSource().stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

}
