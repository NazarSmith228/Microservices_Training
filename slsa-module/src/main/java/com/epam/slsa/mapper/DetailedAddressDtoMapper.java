package com.epam.slsa.mapper;

import com.epam.slsa.dto.address.DetailedAddressDto;
import com.epam.slsa.model.Address;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class DetailedAddressDtoMapper extends PropertyMap<Address, DetailedAddressDto> {

    @Override
    protected void configure() {
        map(source.getLocation().getId(), destination.getLocationId());
    }

}
