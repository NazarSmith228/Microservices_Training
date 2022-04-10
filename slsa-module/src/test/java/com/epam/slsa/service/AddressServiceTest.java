package com.epam.slsa.service;

import com.epam.slsa.dao.AddressDao;
import com.epam.slsa.dto.address.AddressDto;
import com.epam.slsa.dto.address.DetailedAddressDto;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.model.Address;
import com.epam.slsa.service.impl.AddressServiceImpl;
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
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AddressServiceTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressDao addressDao;

    @Mock
    private ModelMapper modelMapper;

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

    private DetailedAddressDto getDetailedAddressDto(int id) {
        return DetailedAddressDto.builder()
                .locationId(id)
                .latitude(145.4)
                .longitude(45.2)
                .build();
    }


    @Test
    public void saveTest() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> addressService.save(getAddressDto()));
    }

    @Test
    public void updateTest() {
        int locationId = 1;
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> addressService.update(getAddressDto(), locationId));
    }

    @Test
    public void getByIdTest() {
        int addressId = 1;

        Address address = getAddress(addressId);

        when(addressDao.getById(addressId)).thenAnswer((Answer<Address>) invocationOnMock ->
                getAddress(invocationOnMock.getArgument(0)));

        when(modelMapper.map(Mockito.any(Address.class), Mockito.eq(AddressDto.class)))
                .thenReturn(getAddressDto());

        AddressDto addressDto = addressService.getById(addressId);

        Assertions.assertTrue(addressDto.getLatitude() == address.getLatitude() &&
                addressDto.getLongitude() == address.getLongitude());
    }

    @Test
    public void getByIdIncorrectTest() {
        int addressId = 1;
        int incorrectId = 100;

        when(addressDao.getById(addressId)).thenAnswer((Answer<Address>) invocationOnMock ->
                getAddress(invocationOnMock.getArgument(0)));

        when(modelMapper.map(Mockito.any(Address.class), Mockito.eq(AddressDto.class)))
                .thenReturn(getAddressDto());

        Assertions.assertThrows(EntityNotFoundException.class, () -> addressService.getById(incorrectId));
    }

    @Test
    public void getByLocationIdTest() {
        int locationId = 1;

        Address address = getAddress(locationId);

        when(addressDao.getByLocationId(locationId)).thenAnswer((Answer<Address>) invocationOnMock ->
                getAddress(invocationOnMock.getArgument(0)));

        when(modelMapper.map(Mockito.any(Address.class), Mockito.eq(AddressDto.class)))
                .thenReturn(getAddressDto());

        AddressDto addressDto = addressService.getByLocationId(locationId);

        Assertions.assertTrue(addressDto.getLatitude() == address.getLatitude() &&
                addressDto.getLongitude() == address.getLongitude());
    }

    @Test
    public void getByLocationIdIncorrectTest() {
        int locationId = 1;
        int incorrectId = 10000;

        when(addressDao.getByLocationId(locationId)).thenAnswer((Answer<Address>) invocationOnMock ->
                getAddress(invocationOnMock.getArgument(0)));

        when(addressDao.getByLocationId(incorrectId)).thenThrow(NoResultException.class);

        when(modelMapper.map(Mockito.any(Address.class), Mockito.eq(AddressDto.class)))
                .thenReturn(getAddressDto());

        Assertions.assertThrows(EntityNotFoundException.class, () -> addressService.getByLocationId(incorrectId));
    }

    @Test
    public void getAllTest() {
        List<Address> addressList = new ArrayList<>();
        addressList.add(getAddress(1));
        addressList.add(getAddress(2));

        when(addressDao.getAll()).thenReturn(addressList);

        when(modelMapper.map(Mockito.any(Address.class), Mockito.eq(AddressDto.class)))
                .thenReturn(getAddressDto());

        List<AddressDto> addressDtoList = addressService.getAll();

        Assertions.assertEquals(addressDtoList.size(), addressList.size());
    }

    @Test
    public void getByCoordinatesTest() {
        int addressId = 1;
        double latitude = 145.4;
        double longitude = 45.2;
        Address address = getAddress(addressId);

        when(addressDao.getByCoordinates(latitude, longitude)).thenReturn(address);

        when(modelMapper.map(Mockito.any(Address.class), Mockito.eq(DetailedAddressDto.class)))
                .thenReturn(getDetailedAddressDto(addressId));

        DetailedAddressDto detailedAddressDto = addressService.getByCoordinates(latitude, longitude);
        Assertions.assertTrue(address.getLatitude() == detailedAddressDto.getLatitude() &&
                address.getLongitude() == detailedAddressDto.getLongitude());
    }

    @Test
    public void getByCoordinatesIncorrectTest() {
        int addressId = 1;
        double latitude = 145.4;
        double longitude = 45.2;
        double incorrectLongitude = 10000000;
        Address address = getAddress(addressId);

        when(addressDao.getByCoordinates(latitude, longitude)).thenReturn(address);

        when(addressDao.getByCoordinates(latitude, incorrectLongitude)).thenThrow(NoResultException.class);

        when(modelMapper.map(Mockito.any(Address.class), Mockito.eq(DetailedAddressDto.class)))
                .thenReturn(getDetailedAddressDto(addressId));

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                addressService.getByCoordinates(latitude, incorrectLongitude));
    }

}
