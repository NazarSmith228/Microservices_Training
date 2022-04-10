package com.epam.spsa.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class StringDurationConverter implements Converter<String, Duration> {

    @Override
    public Duration convert(MappingContext<String, Duration> context) {
        if (context.getSource() == null) {
            return null;
        }
        return Duration.parse(context.getSource());
    }
}
