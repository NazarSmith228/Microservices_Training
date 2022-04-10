package com.epam.spsa.controller;

import com.epam.spsa.dto.address.AddressDto;
import com.epam.spsa.dto.address.DetailedAddressDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.service.AddressService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AddressController.class)
@PropertySource("classpath:/exceptionMessages.properties")
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Value("${address.exception.notfound}")
    private String addressExceptionMessage;

    @Value("${address.coordinates.exception.notfound}")
    private String coordinatesExceptionMessage;

    @Test
    public void getAddressByCorrectIdTest() throws Exception {
        double latitude = 120.0;
        double longitude = 12.5;
        int locationId = 5;

        AddressDto expectedAddressDto = AddressDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();

        when(addressService.getAddressByUserId(eq(locationId)))
                .thenReturn(expectedAddressDto);

        mockMvc.perform(get("/api/v1/users/{id}/address", locationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.longitude").value(longitude))
                .andExpect(jsonPath("$.latitude").value(latitude));
    }

    @Test
    public void getAddressByIncorrectIdTest() throws Exception {
        int id = -1;

        when(addressService.getAddressByUserId(eq(id)))
                .thenThrow(new EntityNotFoundException("Address was not found with id=" + id));

        mockMvc.perform(get("/api/v1/users/{id}/address", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(addressExceptionMessage + id));
    }

    @Test
    public void getAllAddressesTest() throws Exception {
        double latitude = 120.0;
        double longitude = 12.5;

        AddressDto expectedAddressDto = AddressDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();

        when(addressService.getAllAddresses())
                .thenReturn(Lists.list(expectedAddressDto));

        mockMvc.perform(get("/api/v1/addresses/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].longitude").value(longitude))
                .andExpect(jsonPath("$.[0].latitude").value(latitude));
    }

    @Test
    public void getAddressByCoordinatesTest() throws Exception {
        double latitude = 120.0;
        double longitude = 12.5;
        int userId = 5;

        DetailedAddressDto expectedAddressDto = DetailedAddressDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .userId(userId)
                .build();

        when(addressService.getAddressByCoordinates(latitude, longitude))
                .thenReturn(new ArrayList<>(Collections.singletonList(expectedAddressDto)));

        mockMvc.perform(get("/api/v1/addresses/coordinates")
                .param("lat", String.valueOf(latitude))
                .param("long", String.valueOf(longitude)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].longitude").value(longitude))
                .andExpect(jsonPath("$.[0].latitude").value(latitude))
                .andExpect(jsonPath("$.[0].userId").value(userId));
    }

    @Test
    public void getAddressByIncorrectCoordinatesTest() throws Exception {
        double latitude = -11000.1;
        double longitude = -111000.11;

        when(addressService.getAddressByCoordinates(eq(latitude), eq(longitude)))
                .thenThrow(new EntityNotFoundException(coordinatesExceptionMessage));

        mockMvc.perform(get("/api/v1/addresses/coordinates")
                .param("lat", String.valueOf(latitude))
                .param("long", String.valueOf(longitude)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(coordinatesExceptionMessage));
    }

}