package com.epam.spsa.mapper;

import com.epam.spsa.dto.address.DetailedAddressDto;
import com.epam.spsa.model.Address;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class DetailedAddressDtoMapper extends PropertyMap<Address, DetailedAddressDto> {

    @Override
    protected void configure() {
        map(source.getUser().getId(), destination.getUserId());
    }

}
