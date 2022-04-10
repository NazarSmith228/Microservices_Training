package com.epam.slsa.converter;

import com.epam.slsa.dto.link.LinkDto;
import com.epam.slsa.model.Link;
import com.epam.slsa.model.LinkType;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StringLinkTypeConverter implements Converter<Set<LinkDto>, Set<Link>> {

    @Override
    public Set<Link> convert(MappingContext<Set<LinkDto>, Set<Link>> context) {
        return context.getSource()
                .stream()
                .map(l ->
                        Link
                                .builder()
                                .type(LinkType.fromName(l.getType()))
                                .url(l.getUrl())
                                .build()
                ).collect(Collectors.toSet());
    }

}
