package com.epam.slsa.converter;

import com.epam.slsa.model.Placing;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class StringPlacingConverter implements Converter<String, Placing> {

    @Override
    public Placing convert(MappingContext<String, Placing> context) {
        return Placing.getFromName(context.getSource());
    }

}
