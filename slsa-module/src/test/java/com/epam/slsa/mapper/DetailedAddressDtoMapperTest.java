package com.epam.slsa.mapper;

import com.epam.slsa.builders.address.AddressBuilder;
import com.epam.slsa.dto.address.DetailedAddressDto;
import com.epam.slsa.model.Address;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DetailedAddressDtoMapperTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void DetailedAddressMapperTest() {
        Address address = AddressBuilder.getAddress();
        DetailedAddressDto detailedAddressDto = modelMapper.map(address, DetailedAddressDto.class);
        DetailedAddressDto newDetailedAddressDto = AddressBuilder.getDetailedAddressDtoForMappingTest();
        assertEquals(detailedAddressDto.toString(), newDetailedAddressDto.toString());
    }

}
