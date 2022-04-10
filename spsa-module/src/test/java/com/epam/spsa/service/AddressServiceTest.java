package com.epam.spsa.service;

import com.epam.spsa.dao.AddressDao;
import com.epam.spsa.dto.address.AddressDto;
import com.epam.spsa.dto.address.DetailedAddressDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.Address;
import com.epam.spsa.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AddressServiceTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AddressDao addressDao;

    private Address getAddress(int id) {
        return Address.builder()
                .id(id)
                .latitude(145.4)
                .longitude(45.2)
                .build();
    }

    private AddressDto getAddressDto() {
        return AddressDto.builder()
                .latitude(145.4)
                .longitude(45.2)
                .build();
    }

    @Test
    public void getAllAddressesTest() {
        List<Address> addressList = new ArrayList<>();
        addressList.add(getAddress(0));
        addressList.add(getAddress(1));

        when(addressDao.getAll()).thenReturn(addressList);

        when(modelMapper.map(Mockito.any(Address.class), Mockito.eq(AddressDto.class))).thenReturn(getAddressDto());

        List<AddressDto> addressDtoList = addressService.getAllAddresses();

        Assertions.assertEquals(addressList.size(), addressDtoList.size());
    }

    @Test
    public void getAddressByCoordinatesTest() {
        double latitude = 145.4;
        double longitude = 45.2;
        Address address = getAddress(1);

        Address[] addresses = {address};

        when(addressDao.getByCoordinates(latitude, longitude)).thenReturn(Arrays.asList(addresses));

        when(modelMapper.map(Mockito.any(Address.class), Mockito.any())).thenAnswer(
                (Answer<DetailedAddressDto>) invocationOnMock -> {
                    Address address1 = invocationOnMock.getArgument(0);
                    return DetailedAddressDto.builder()
                            .userId(address1.getId())
                            .latitude(address1.getLatitude())
                            .longitude(address1.getLongitude())
                            .build();
                });

        List<DetailedAddressDto> addressDtoList = addressService.getAddressByCoordinates(latitude, longitude);
        Assertions.assertEquals(address.getLatitude(), addressDtoList.get(0).getLatitude());
        Assertions.assertEquals(address.getLongitude(), addressDtoList.get(0).getLongitude());
    }

    @Test
    public void getAddressByCoordinatesIncorrectTest() {
        double latitude = 145.4;
        double longitude = 45.2;

        Address address = getAddress(1);
        Address[] addresses = {address};

        when(addressDao.getByCoordinates(latitude, longitude)).thenReturn(Arrays.asList(addresses));

        assertThrows(EntityNotFoundException.class,
                () -> addressService.getAddressByCoordinates(49.83862, 24.0353));
    }

    @Test
    public void getAddressByUserIdTest() {
        int userId = 1;
        Address address = getAddress(1);

        when(addressDao.getByUserId(userId)).thenAnswer(
                (Answer<Address>) invocationOnMock -> getAddress(invocationOnMock.getArgument(0)));

        when(modelMapper.map(Mockito.any(Address.class), Mockito.eq(AddressDto.class)))
                .thenReturn(getAddressDto());

        AddressDto addressDto = addressService.getAddressByUserId(userId);
        Assertions.assertTrue(address.getLatitude() == addressDto.getLatitude() &&
                address.getLongitude() == addressDto.getLongitude());

    }

    @Test
    public void getAddressByIncorrectUserIdTest() {
        int id = -1;
        when(addressDao.getByUserId(id)).thenThrow(NoResultException.class);

        assertThrows(EntityNotFoundException.class, () -> {
            addressService.getAddressByUserId(id);
        });
    }

}