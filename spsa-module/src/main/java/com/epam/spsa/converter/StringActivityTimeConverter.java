package com.epam.spsa.converter;

import com.epam.spsa.model.ActivityTime;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class StringActivityTimeConverter implements Converter<String, ActivityTime> {

    @Override
    public ActivityTime convert(MappingContext<String, ActivityTime> context) {
        if (context.getSource() != null) {
            return ActivityTime.getFromName(context.getSource());
        }
        return null;
    }

}