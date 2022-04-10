package com.epam.spsa.converter;

import com.epam.spsa.model.AuthProvider;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class StringAuthProviderConverter implements Converter<String, AuthProvider> {

    @Override
    public AuthProvider convert(MappingContext<String, AuthProvider> context) {
        return AuthProvider.fromName(context.getSource());
    }

}
