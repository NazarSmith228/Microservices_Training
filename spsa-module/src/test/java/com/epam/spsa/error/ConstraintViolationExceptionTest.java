package com.epam.spsa.error;

import com.epam.spsa.controller.UserController;
import com.epam.spsa.controller.builder.UserBuilder;
import com.epam.spsa.dao.SportTypeDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.address.AddressDto;
import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.dto.user.UserDto;
import com.epam.spsa.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class ConstraintViolationExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDao userDao;

    @MockBean
    private SportTypeDao sportTypeDao;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private UserService userService;


    @Test
    public void saveInvalidUserTest() throws Exception {
        UserDto user = UserBuilder.getUserDto();
        user.setDateOfBirth("2000-05-2000");
        user.setGender("kebab");
        user.setName("Nazar228");

        AddressDto address = new AddressDto(-2000.0, 20000.0);
        user.setAddress(address);

        when(userDao.getByPhoneNumber(user.getPhoneNumber()))
                .thenThrow(NoResultException.class);

        when(userDao.getByEmail(user.getEmail()))
                .thenThrow(NoResultException.class);

        when(userService.save(Mockito.any(UserDto.class)))
                .thenThrow(ConstraintViolationException.class);

        mockMvc.perform(post("/api/v1/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .characterEncoding("UTF-8"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error!"))

                .andExpect(jsonPath("$.subErrors[?(@.field=='dateOfBirth')].message")
                        .value("Incorrect date format"))
                .andExpect(jsonPath("$.subErrors[?(@.field=='dateOfBirth')].rejectedValue").value(user.getDateOfBirth()))

                .andExpect(jsonPath("$.subErrors[?(@.field=='name')].rejectedValue").value(user.getName()))
                .andExpect(jsonPath("$.subErrors[?(@.field=='name')].message").value("Incorrect name"))

                .andExpect(jsonPath("$.subErrors[?(@.field=='address.latitude')].message")
                        .value("Address` latitude must be >= -180.0"))
                .andExpect(jsonPath("$.subErrors[?(@.field=='address.latitude')].rejectedValue")
                        .value(user.getAddress().getLatitude()))

                .andExpect(jsonPath("$.subErrors[?(@.field=='address.longitude')].message")
                        .value("Address` longitude must be <= 180.0"))
                .andExpect(jsonPath("$.subErrors[?(@.field=='address.longitude')].rejectedValue")
                        .value(user.getAddress().getLongitude()));
    }

    @Test
    public void updateUserWithInvalidValue() throws Exception {
        UserDto oldUser = UserBuilder.getUserDto();
        oldUser.setEmail("lol");
        oldUser.setPhoneNumber("0679359820");

        SportTypeDto sportType = new SportTypeDto(-1, "Programming");

        when(userDao.getByEmail(oldUser.getEmail()))
                .thenThrow(NoResultException.class);

        when(userService.update(Mockito.any(UserDto.class), Mockito.anyInt()))
                .thenThrow(ConstraintViolationException.class);

        mockMvc.perform(put("/api/v1/users/{id}", 77)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(oldUser))
                .characterEncoding("UTF-8"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.subErrors[?(@.field=='email')].message").value("Incorrect email"))
                .andExpect(jsonPath("$.subErrors[?(@.field=='email')].rejectedValue").value("lol"));
    }

}