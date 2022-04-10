package com.epam.slsa.converter;

import com.epam.slsa.model.Gender;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class StringGenderConverter implements Converter<String, Gender> {

    @Override
    public Gender convert(MappingContext<String, Gender> context) {
        return Gender.fromName(context.getSource());
    }

}
