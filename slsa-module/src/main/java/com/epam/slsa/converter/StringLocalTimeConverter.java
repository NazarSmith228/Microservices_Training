package com.epam.slsa.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class StringLocalTimeConverter implements Converter<String, LocalTime> {

    @Override
    public LocalTime convert(MappingContext<String, LocalTime> mappingContext) {
        return LocalTime.parse(mappingContext.getSource());
    }

}
