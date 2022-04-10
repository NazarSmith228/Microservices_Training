package com.epam.slsa.converter;

import com.epam.slsa.model.Day;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class StringDayConverter implements Converter<String, Day> {

    @Override
    public Day convert(MappingContext<String, Day> context) {
        return Day.fromName(context.getSource());
    }

}
