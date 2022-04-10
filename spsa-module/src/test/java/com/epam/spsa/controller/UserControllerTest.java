
package com.epam.spsa.controller;

import com.epam.spsa.controller.builder.UserBuilder;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.user.MainUserDto;
import com.epam.spsa.dto.user.UserDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.imageBuilder.FileBuilder;
import com.epam.spsa.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.NoResultException;

import static com.epam.spsa.controller.builder.UserBuilder.getMainUserDto;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@PropertySource("classpath:/exceptionMessages.properties")
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @Value("${user.exception.notfound}")
    private String userExceptionMessage;

    @Test
    public void saveUserWithoutRequestBodyTest() throws Exception {
        MainUserDto newUserDto = new MainUserDto();

        when(userService.save(any(UserDto.class)))
                .thenReturn(5);

        mockMvc.perform(post("/api/v1/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserDto))
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveValidUserTest() throws Exception {
        UserDto user = UserBuilder.getUserDto();

        when(userDao.getByPhoneNumber(user.getPhoneNumber()))
                .thenThrow(NoResultException.class);

        when(userDao.getByEmail(user.getEmail()))
                .thenThrow(NoResultException.class);

        when(userService.save(any(UserDto.class)))
                .thenReturn(5);

        mockMvc.perform(post("/api/v1/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .characterEncoding("UTF-8"))
                .andExpect(status().isCreated());
    }

    @Test
    public void getUserByCorrectIdTest() throws Exception {
        MainUserDto userDto = getMainUserDto();

        when(userService.getById(anyInt()))
                .thenReturn(userDto);

        mockMvc.perform(get("/api/v1/users/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.surname").value(userDto.getSurname()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(userDto.getPhoneNumber()))
                .andExpect(jsonPath("$.dateOfBirth").value(userDto.getDateOfBirth()))
                .andExpect(jsonPath("$.address").value(userDto.getAddress()))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.hasChildren").value(userDto.isHasChildren()));
    }

    @Test
    public void getUserByIncorrectId() throws Exception {
        int id = -1;

        when(userService.getById(id))
                .thenThrow(new EntityNotFoundException(userExceptionMessage + id));

        mockMvc.perform(get("/api/v1/users/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(userExceptionMessage + id));
    }

    @Test
    public void updateValidUserTest() throws Exception {
        UserDto user = UserBuilder.getUserDto();

        setBehaviour(user);

        when(userService.update(any(UserDto.class), anyInt()))
                .thenReturn(user);

        mockMvc.perform(put("/api/v1/users/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }


    @Test
    public void deleteUserByCorrectId() throws Exception {
        int id = 11;

        doNothing().when(userService).delete(id);

        mockMvc.perform(delete("/api/v1/users/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserByIncorrectId() throws Exception {
        int id = -1;

        doThrow(new EntityNotFoundException(userExceptionMessage + id))
                .when(userService).delete(id);

        mockMvc.perform(delete("/api/v1/users/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(userExceptionMessage + id));
    }

    @Test
    public void getAllUsersTest() throws Exception {
        MainUserDto mainUserDto = getMainUserDto();

        when(userService.getAll())
                .thenReturn(Lists.list(mainUserDto));

        mockMvc.perform(get("/api/v1/users/"))
                .andExpect(status().isOk());

    }

    @Test
    public void saveImageByUserIdTest() throws Exception {
        int id = 1;
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(userService.saveImageByUserId(multipartFile, id))
                .thenAnswer((Answer<MainUserDto>) invocationOnMock -> MainUserDto
                        .builder()
                        .photo("https:...")
                        .build());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/users/{id}/image", id)
                .file(multipartFile))
                .andExpect(jsonPath("$.photo").value("https:..."))
                .andExpect(status().isCreated());
    }

    @Test
    public void saveImageByUserIdIncorrectTest() throws Exception {
        int id = 1;
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "txtTest.txt", "txt/plane", FileBuilder.getTxtBytes());

        Mockito.when(userService.saveImageByUserId(multipartFile, id))
                .thenAnswer((Answer<MainUserDto>) invocationOnMock -> MainUserDto
                        .builder()
                        .photo("https:...")
                        .build());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/users/{id}/image", id)
                .file(multipartFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveUrlImageByUserIdTest() throws Exception {
        int id = 1;
        String url = "https:...";
        Mockito.when(userService.saveImageByUserId(url, id))
                .thenAnswer((Answer<MainUserDto>) invocationOnMock -> MainUserDto
                        .builder()
                        .photo(invocationOnMock.getArgument(0))
                        .build());

        mockMvc.perform(post("/api/v1/users/{id}/image-url", id)
                .param("url", url))
                .andExpect(jsonPath("$.photo").value("https:..."))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteImageByUserIdTest() throws Exception {
        int id = 1;
        Mockito.doNothing().when(userService).deleteImageByUserId(id);

        mockMvc.perform(delete("/api/v1/users/{id}/image", id))
                .andExpect(status().isOk());
    }

    @Test
    public void updateImageByUserIdTest() throws Exception {
        int id = 1;
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(userService.updateImageByUserId(multipartFile, id))
                .thenAnswer((Answer<MainUserDto>) invocationOnMock -> MainUserDto
                        .builder()
                        .photo("https:...")
                        .build());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/v1/users/{id}/image", id);

        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(builder
                .file(multipartFile))
                .andExpect(jsonPath("$.photo").value("https:..."))
                .andExpect(status().isOk());
    }

    @Test
    public void updateImageByUserId() throws Exception {
        int id = 1;
        String url = "https:...";

        Mockito.when(userService.updateImageByUserId(url, id))
                .thenAnswer((Answer<MainUserDto>) invocationOnMock -> MainUserDto.builder()
                        .photo(invocationOnMock.getArgument(0))
                        .build());

        mockMvc.perform(put("/api/v1/users/{id}/url-image", id)
                .param("url", url))
                .andExpect(jsonPath("$.photo").value(url))
                .andExpect(status().isOk());
    }

    private void setBehaviour(UserDto user) {

        when(userDao.getByPhoneNumber(user.getPhoneNumber()))
                .thenThrow(NoResultException.class);

        when(userDao.getByEmail(user.getEmail()))
                .thenThrow(NoResultException.class);
    }
}