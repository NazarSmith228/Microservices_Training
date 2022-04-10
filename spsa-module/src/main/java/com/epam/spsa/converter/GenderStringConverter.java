package com.epam.spsa.converter;

import com.epam.spsa.model.Gender;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class GenderStringConverter implements Converter<Gender, String> {

    @Override
    public String convert(MappingContext<Gender, String> context) {
        return context.getSource().getGender();
    }

}
