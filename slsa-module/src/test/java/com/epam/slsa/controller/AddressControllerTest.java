package com.epam.slsa.controller;

import com.epam.slsa.builders.address.AddressBuilder;
import com.epam.slsa.builders.address.AddressInfo;
import com.epam.slsa.dto.address.AddressDto;
import com.epam.slsa.dto.address.DetailedAddressDto;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    private AddressDto addressDto;
    private DetailedAddressDto detailedAddressDto;
    private double latitude;
    private double longitude;
    private int locationId;

    @BeforeEach
    public void setUpVariables() {
        latitude = AddressInfo.latitude;
        longitude = AddressInfo.longitude;
        locationId = AddressInfo.locationId;
        addressDto = AddressBuilder.getAddressDto();
        detailedAddressDto = AddressBuilder.getDetailedAddressDto();
    }

    @BeforeEach
    public void setUpMockito() {
        Mockito.when(addressService.getByLocationId(ArgumentMatchers.eq(locationId))).thenReturn(addressDto);
        Mockito.when(addressService.getByLocationId(ArgumentMatchers.eq(locationId))).thenReturn(addressDto);
        Mockito.when(addressService.getByLocationId(ArgumentMatchers.eq(-1))).thenThrow(EntityNotFoundException.class);
        Mockito.when(addressService.getByCoordinates(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyDouble())).thenReturn(detailedAddressDto);
    }

    @Test
    public void getByLocationId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/locations/{id}/address", AddressInfo.locationId))
                .andExpect(jsonPath("$.longitude").value(longitude))
                .andExpect(jsonPath("$.latitude").value(latitude))
                .andExpect(status().isOk());
    }

    @Test
    public void getByInvalidLocationId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/locations/{id}/address", -1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getDetailedAddressDtoByCoordinates() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/addresses/coordinates/?lat=10&long=20"))
                .andExpect(jsonPath("$.locationId").value(locationId))
                .andExpect(jsonPath("$.longitude").value(longitude))
                .andExpect(jsonPath("$.latitude").value(latitude))
                .andExpect(status().isOk());
    }

    @Test
    public void getDetailedAddressDtoWithoutCoordinates() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/addresses/coordinates"))
                .andExpect(status().isBadRequest());
    }

}