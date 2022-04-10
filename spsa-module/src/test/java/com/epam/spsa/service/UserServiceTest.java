package com.epam.spsa.service;

import com.epam.spsa.dao.RoleDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.address.AddressDto;
import com.epam.spsa.dto.user.MainUserDto;
import com.epam.spsa.dto.user.UserDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.PhotoException;
import com.epam.spsa.feign.MailClient;
import com.epam.spsa.feign.SlsaClient;
import com.epam.spsa.imageBuilder.FileBuilder;
import com.epam.spsa.model.Address;
import com.epam.spsa.model.Gender;
import com.epam.spsa.model.Role;
import com.epam.spsa.model.User;
import com.epam.spsa.s3api.S3Manager;
import com.epam.spsa.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDao userDao;

    @Mock
    private RoleDao roleDao;

    @Mock
    private ModelMapper mapper;

    @Mock
    private S3Manager manager;

    @Mock
    private MailClient mailClient;

    @Mock
    private SlsaClient slsaClient;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    UserStatsService statsService;

    @Test
    public void saveTest() {
        UserDto userDto = getUserDto();

        Mockito.when(mapper.map(userDto, User.class)).thenReturn(getUser(0));

        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("encoded");

        Mockito.when(userDao.save(Mockito.any(User.class))).thenAnswer(
                (Answer<User>) invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0);
                    user.setId(666);
                    return user;
                });

        Mockito.when(roleDao.getByName(Mockito.any())).thenReturn(
                Role.builder()
                        .id(1)
                        .name("USER")
                        .build());

        Mockito.when(mapper.map(Mockito.any(User.class), Mockito.eq(UserDto.class))).thenReturn(getUserDto());

        int userId = userService.save(userDto);

        assertEquals(userId, 666);
    }

    @Test
    public void deleteTest() {
        int id = 1;

        Mockito.when(userDao.getById(id)).thenReturn(getUser(id));

        Mockito.doNothing().when(mailClient).sendEmail("name", "email", "message");

        Mockito.doNothing().when(userDao).delete(Mockito.any(User.class));

        Mockito.doNothing().when(slsaClient).deleteCommentByUserId(id);

        assertDoesNotThrow(() -> {
            userService.delete(id);
        });
    }

    @Test
    public void updateTest() {
        int id = 1;
        UserDto editedUserDto = getUserDto();

        editedUserDto.setDateOfBirth("2000-05-30");

        Mockito.when(userDao.getById(id)).thenReturn(getUser(id));

        Mockito.when(mapper.map(Mockito.any(UserDto.class), Mockito.eq(User.class)))
                .thenAnswer((Answer<User>) invocationOnMock -> {
                    UserDto userDto = invocationOnMock.getArgument(0);
                    User user = invocationOnMock.getArgument(1);
                    user.setDateOfBirth(LocalDate.parse(userDto.getDateOfBirth()));
                    user.setPassword(userDto.getPassword());
                    return user;
                });

        Mockito.when(userDao.update(Mockito.any(User.class))).thenAnswer(
                (Answer<User>) invocationOnMock ->
                        invocationOnMock.getArgument(0));

        Mockito.when(mapper.map(Mockito.any(User.class), Mockito.eq(UserDto.class))).thenAnswer(
                (Answer<UserDto>) invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0);
                    UserDto userDto = getUserDto();
                    userDto.setDateOfBirth("2000-05-30");
                    userDto.setPassword("password");
                    return userDto;
                });

        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);

        UserDto editedUser = userService.update(editedUserDto, id);

        assertEquals(editedUser, editedUserDto);
    }

    @Test
    public void getByIdTest() {
        int id = 1;

        Mockito.when(userDao.getById(id)).thenReturn(getUser(id));

        Mockito.when(mapper.map(Mockito.any(User.class), Mockito.eq(MainUserDto.class))).thenAnswer(
                (Answer<MainUserDto>) invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0);
                    return getMainUser(user.getId());
                });

        MainUserDto userDto = userService.getById(id);

        assertEquals(userDto.getId(), id);
    }

    @Test
    public void getByIdIncorrectTest() {
        int id = 1;

        Mockito.when(userDao.getById(id)).thenReturn(getUser(id));

        assertThrows(EntityNotFoundException.class,
                () -> userService.getById(666));
    }

    @Test
    public void getAllTest() {
        User userEx = getUser(1);
        User[] users = {userEx};

        Mockito.when(userDao.getAll()).thenReturn(Arrays.asList(users));

        Mockito.when(mapper.map(Mockito.any(User.class), Mockito.eq(MainUserDto.class))).thenAnswer(
                (Answer<MainUserDto>) invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0);
                    return getMainUser(user.getId());
                });

        assertTrue(userService.getAll().size() > 0);
    }

    @Test
    public void saveImageByUserIdTest() throws IOException {
        int userId = 1;

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(userDao.getById(userId))
                .thenAnswer((Answer<User>) invocationOnMock -> getUser(invocationOnMock.getArgument(0)));

        Mockito.when(manager.saveImageById(multipartFile, userId, "user"))
                .thenReturn("https:...");

        Mockito.when(userDao.update(Mockito.any(User.class)))
                .then((Answer<User>) invocationOnMock -> invocationOnMock.getArgument(0));

        Mockito.when(mapper.map(Mockito.any(User.class), Mockito.eq(MainUserDto.class)))
                .thenAnswer((Answer<MainUserDto>) invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0);
                    MainUserDto mainUserDto = getMainUser(user.getId());
                    mainUserDto.setPhoto(user.getPhoto());
                    return mainUserDto;
                });

        MainUserDto mainUserDto = userService.saveImageByUserId(multipartFile, userId);
        Assertions.assertEquals("https:...", mainUserDto.getPhoto());
    }

    @Test
    public void saveImageByUserIdIncorrectTest() throws IOException {
        int userId = 1;
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.saveImageByUserId(multipartFile, userId));
    }

    @Test
    public void saveImageByUserIdIncorrectTest2() throws IOException {
        int userId = 1;
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(userDao.getById(userId))
                .thenAnswer((Answer<User>) invocationOnMock -> {
                    User user = getUser(invocationOnMock.getArgument(0));
                    user.setPhoto("254");
                    return user;
                });

        Assertions.assertThrows(PhotoException.class, () -> userService.saveImageByUserId(multipartFile, userId));
    }

    @Test
    public void saveUrlImageByUserIdTest() {
        int userId = 1;
        String url = "https:...";

        Mockito.when(userDao.getById(userId))
                .thenAnswer((Answer<User>) invocationOnMock -> getUser(invocationOnMock.getArgument(0)));

        Mockito.when(userDao.update(Mockito.any(User.class)))
                .then((Answer<User>) invocationOnMock -> invocationOnMock.getArgument(0));

        Mockito.when(mapper.map(Mockito.any(User.class), Mockito.eq(MainUserDto.class)))
                .thenAnswer((Answer<MainUserDto>) invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0);
                    MainUserDto mainUserDto = getMainUser(user.getId());
                    mainUserDto.setPhoto(user.getPhoto());
                    return mainUserDto;
                });

        MainUserDto mainUserDto = userService.saveImageByUserId(url, userId);
        Assertions.assertEquals(url, mainUserDto.getPhoto());
    }

    @Test
    public void saveUrlImageByUserIdIncorrectTest() {
        int userId = 1;
        String url = "https:...";

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.saveImageByUserId(url, userId));
    }

    @Test
    public void saveUrlImageByUserIdIncorrectTest2() {
        int userId = 1;
        String url = "https:...";

        Mockito.when(userDao.getById(userId))
                .thenAnswer((Answer<User>) invocationOnMock -> {
                    User user = getUser(invocationOnMock.getArgument(0));
                    user.setPhoto("254");
                    return user;
                });

        Assertions.assertThrows(PhotoException.class,
                () -> userService.saveImageByUserId(url, userId));
    }

    @Test
    public void deleteImageByUserIdTest() {
        int userId = 1;

        Mockito.when(userDao.getById(userId))
                .thenAnswer((Answer<User>) invocationOnMock -> getUser(invocationOnMock.getArgument(0)));

        Mockito.doNothing().when(manager).deleteFileByUserId(userId, "user");

        Mockito.when(userDao.update(Mockito.any(User.class)))
                .then((Answer<User>) invocationOnMock -> invocationOnMock.getArgument(0));

        Assertions.assertDoesNotThrow(() -> userService.deleteImageByUserId(userId));
    }

    @Test
    public void deleteImageByUserIdIncorrectTest() {
        int userId = 1;

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.deleteImageByUserId(userId));
    }

    @Test
    public void updateImageByUserIdTest() throws IOException {
        int userId = 1;

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(userDao.getById(userId))
                .thenAnswer((Answer<User>) invocationOnMock -> {
                    User user = getUser(invocationOnMock.getArgument(0));
                    user.setPhoto("bgdvfs");
                    return user;
                });

        Mockito.when(manager.saveImageById(multipartFile, userId, "user"))
                .thenReturn("https:...");

        Mockito.when(userDao.update(Mockito.any(User.class)))
                .then((Answer<User>) invocationOnMock -> invocationOnMock.getArgument(0));

        Mockito.when(mapper.map(Mockito.any(User.class), Mockito.eq(MainUserDto.class)))
                .thenAnswer((Answer<MainUserDto>) invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0);
                    MainUserDto mainUserDto = getMainUser(user.getId());
                    mainUserDto.setPhoto(user.getPhoto());
                    return mainUserDto;
                });

        MainUserDto mainUserDto = userService.updateImageByUserId(multipartFile, userId);
        Assertions.assertEquals("https:...", mainUserDto.getPhoto());
    }

    @Test
    public void updateImageByUserIdIncorrectTest() throws IOException {
        int userId = 1;

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.updateImageByUserId(multipartFile, userId));
    }

    @Test
    public void updateImageByUserIdIncorrectTest2() throws IOException {
        int userId = 1;

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(userDao.getById(userId))
                .thenAnswer((Answer<User>) invocationOnMock -> getUser(invocationOnMock.getArgument(0)));

        Assertions.assertThrows(PhotoException.class,
                () -> userService.updateImageByUserId(multipartFile, userId));
    }

    @Test
    public void updateUrlImageByUserIdTest() {
        int userId = 1;
        String url = "https:...";

        Mockito.when(userDao.getById(userId))
                .thenAnswer((Answer<User>) invocationOnMock -> {
                    User user = getUser(invocationOnMock.getArgument(0));
                    user.setPhoto("bgdvfs");
                    return user;
                });

        Mockito.when(userDao.update(Mockito.any(User.class)))
                .then((Answer<User>) invocationOnMock -> invocationOnMock.getArgument(0));

        Mockito.when(mapper.map(Mockito.any(User.class), Mockito.eq(MainUserDto.class)))
                .thenAnswer((Answer<MainUserDto>) invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0);
                    MainUserDto mainUserDto = getMainUser(user.getId());
                    mainUserDto.setPhoto(user.getPhoto());
                    return mainUserDto;
                });

        MainUserDto mainUserDto = userService.updateImageByUserId(url, userId);
        Assertions.assertEquals(url, mainUserDto.getPhoto());
    }

    @Test
    public void updateUrlImageByUserIdIncorrectTest() {
        int userId = 1;
        String url = "https:...";

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.updateImageByUserId(url, userId));
    }

    @Test
    public void updateUrlImageByUserIdTest2() {
        int userId = 1;
        String url = "https:...";

        Mockito.when(userDao.getById(userId))
                .thenAnswer((Answer<User>) invocationOnMock -> getUser(invocationOnMock.getArgument(0)));

        Assertions.assertThrows(PhotoException.class,
                () -> userService.updateImageByUserId(url, userId));
    }

    private UserDto getUserDto() {
        return UserDto.builder()
                .name("Maksym")
                .surname("Natural")
                .dateOfBirth("2000-05-30")
                .email("210372@ukr.net")
                .password("password")
                .address(AddressDto.builder()
                        .latitude(42.14)
                        .longitude(75.2)
                        .build())
                .gender("Male")
                .hasChildren(false)
                .phoneNumber("0980265499")
                .build();
    }

    private MainUserDto getMainUserDto() {
        return MainUserDto.builder()
                .id(1)
                .name("Maksym")
                .surname("Natural")
                .dateOfBirth("2000-05-30")
                .email("210372@ukr.net")
                .address(AddressDto.builder()
                        .latitude(42.14)
                        .longitude(75.2)
                        .build())
                .gender(Gender.MALE.toString())
                .hasChildren(false)
                .phoneNumber("0980265499")
                .build();
    }

    private User getUser(int id) {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().id(1).name("USER").build());
        return User.builder()
                .id(id)
                .name("Maksym")
                .surname("Natural")
                .dateOfBirth(LocalDate.of(2000, 5, 30))
                .email("210372@ukr.net")
                .address(Address.builder()
                        .latitude(42.14)
                        .longitude(75.2)
                        .build())
                .gender(Gender.MALE)
                .hasChildren(false)
                .phoneNumber("0980265499")
                .roles(roles)
                .build();
    }

    private MainUserDto getMainUser(int id) {
        return MainUserDto.builder()
                .id(id)
                .name("Maksym")
                .surname("Natural")
                .dateOfBirth("2000-05-30")
                .email("210372@ukr.net")
                .address(AddressDto.builder()
                        .latitude(42.14)
                        .longitude(75.2)
                        .build())
                .gender(Gender.MALE.toString())
                .hasChildren(false)
                .phoneNumber("0980265499")
                .build();
    }

}