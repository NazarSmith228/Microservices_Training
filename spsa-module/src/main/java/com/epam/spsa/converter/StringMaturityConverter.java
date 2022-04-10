package com.epam.spsa.converter;

import com.epam.spsa.model.Maturity;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class StringMaturityConverter implements Converter<String, Maturity> {

    @Override
    public Maturity convert(MappingContext<String, Maturity> context) {
        if (context.getSource() != null) {
            return Maturity.getFromName(context.getSource());
        }
        return null;
    }

}
