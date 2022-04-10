package com.epam.slsa.builders.address;

import com.epam.slsa.dto.address.AddressDto;
import com.epam.slsa.dto.address.DetailedAddressDto;
import com.epam.slsa.model.Address;

public class AddressBuilder {
    public static DetailedAddressDto getDetailedAddressDto() {
        return DetailedAddressDto.builder()
                .longitude(AddressInfo.longitude)
                .latitude(AddressInfo.latitude)
                .locationId(AddressInfo.locationId)
                .build();
    }

    public static DetailedAddressDto getDetailedAddressDtoForMappingTest() {
        return DetailedAddressDto.builder()
                .longitude(AddressInfo.longitude)
                .latitude(AddressInfo.latitude)
                .locationId(0)
                .build();
    }

    public static AddressDto getAddressDto() {
        return AddressDto.builder()
                .longitude(AddressInfo.longitude)
                .latitude(AddressInfo.latitude)
                .build();
    }

    public static Address getAddress() {
        return Address.builder()
                .location(null)
                .longitude(AddressInfo.longitude)
                .latitude(AddressInfo.latitude)
                .build();
    }

}
