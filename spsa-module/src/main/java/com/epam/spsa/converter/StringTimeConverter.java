package com.epam.spsa.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.sql.Time;

@Component
public class StringTimeConverter implements Converter<String, Time> {

    @Override
    public Time convert(MappingContext<String, Time> mappingContext) {
        return Time.valueOf(mappingContext.getSource());
    }

}
