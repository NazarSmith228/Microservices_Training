package com.epam.spsa.converter;

import com.epam.spsa.model.Gender;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class StringGenderConverter implements Converter<String, Gender> {

    @Override
    public Gender convert(MappingContext<String, Gender> context) {
        if (context.getSource() != null) {
            return Gender.getFromName(context.getSource());
        }
        return null;
    }

}
