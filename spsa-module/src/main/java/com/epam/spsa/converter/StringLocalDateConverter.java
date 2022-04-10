package com.epam.spsa.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StringLocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(MappingContext<String, LocalDate> context) {
        if (context.getSource() == null) {
            return null;
        }
        return LocalDate.parse(context.getSource());
    }

}
