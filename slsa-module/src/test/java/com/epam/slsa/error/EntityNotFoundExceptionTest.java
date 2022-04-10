package com.epam.slsa.error;

import com.epam.slsa.controller.AddressController;
import com.epam.slsa.controller.CoachController;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.service.AddressService;
import com.epam.slsa.service.CoachService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CoachController.class, AddressController.class})
@AutoConfigureMockMvc(addFilters = false)
public class EntityNotFoundExceptionTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @MockBean
    private CoachService coachService;

    @Value("${location.exception.notfound}")
    private String locationExceptionMessage;

    @Value("${address.coordinates.exception.notfound}")
    private String coordinatesExceptionMessage;

    @Value("${coach.exception.notfound}")
    private String coachExceptionMessage;

    @Test
    public void getAddressByIncorrectIdTest() throws Exception {
        int id = -1;

        when(addressService.getByLocationId(ArgumentMatchers.eq(id)))
                .thenThrow(new EntityNotFoundException(locationExceptionMessage + id))
                .thenReturn(null);

        mockMvc.perform(get("/api/v1/locations/{id}/address", id))
                .andExpect(jsonPath("$.message").value(locationExceptionMessage + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCoachByIncorrectIdTest() throws Exception {
        int coachId = -2;

        doThrow(new EntityNotFoundException(coachExceptionMessage + coachId))
                .when(coachService)
                .delete(coachId);

        mockMvc.perform(delete("/api/v1/coaches/{coachId}", coachId))
                .andExpect(jsonPath("$.message").value(coachExceptionMessage + coachId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCoachByIncorrectIdTest() throws Exception {
        int coachId = -2;

        doThrow(new EntityNotFoundException(coachExceptionMessage + coachId))
                .when(coachService)
                .delete(coachId);

        mockMvc.perform(delete("/api/v1/coaches/{coachId}", coachId))
                .andExpect(jsonPath("$.message").value(coachExceptionMessage + coachId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAddressByIncorrectCoordinatesTest() throws Exception {
        double latitude = -11.1;
        double longitude = -111.11;

        when(addressService.getByCoordinates(Mockito.eq(latitude), Mockito.eq(longitude)))
                .thenThrow(new EntityNotFoundException(coordinatesExceptionMessage));

        mockMvc.perform(get("/api/v1/addresses/coordinates")
                .param("lat", String.valueOf(latitude))
                .param("long", String.valueOf(longitude)))
                .andExpect(jsonPath("$.message").value(coordinatesExceptionMessage))
                .andExpect(status().isNotFound());
    }

}
