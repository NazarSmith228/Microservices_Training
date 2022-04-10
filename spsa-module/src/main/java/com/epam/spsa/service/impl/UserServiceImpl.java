package com.epam.spsa.service.impl;

import com.epam.spsa.dao.ChatDao;
import com.epam.spsa.dao.RoleDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.security.RoleDto;
import com.epam.spsa.dto.user.MainUserDto;
import com.epam.spsa.dto.user.UserDto;
import com.epam.spsa.error.exception.EntityAlreadyExistsException;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.EntityRoleException;
import com.epam.spsa.error.exception.PhotoException;
import com.epam.spsa.feign.MailClient;
import com.epam.spsa.feign.SlsaClient;
import com.epam.spsa.model.AuthProvider;
import com.epam.spsa.model.Role;
import com.epam.spsa.model.User;
import com.epam.spsa.s3api.S3Manager;
import com.epam.spsa.security.oauth2.user.UserPrincipal;
import com.epam.spsa.service.UserService;
import com.epam.spsa.service.UserStatsService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PropertySource(value = "classpath:/exceptionMessages.properties")
@Slf4j
@SuppressWarnings("Duplicates")
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final RoleDao roleDao;

    private final ChatDao chatDao;

    private final UserStatsService statsService;

    private final ModelMapper mapper;

    private final S3Manager manager;

    private final MailClient mailClient;

    private final SlsaClient slsaClient;

    private final PasswordEncoder passwordEncoder;

    @Value("${user.exception.notfound}")
    private String userNotFoundByIdMessage;

    @Value("${user.email.notfound}")
    private String userNotFoundByEmailMessage;

    @Value("${user.phoneNumber.notfound}")
    private String userNotFoundByPhoneNumberMessage;

    @Value("${user.photo.exception.alreadyExists}")
    private String photoTiedMessage;

    @Value("${user.photo.exception.notExists}")
    private String photoNotTiedMessage;

    @Value("${user.email.exists}")
    private String emailAlreadyExistsMessage;

    @Value("${user.phoneNumber.exists}")
    private String phoneNumberAlreadyExistsMessage;

    @Value("${user.auth.notFound}")
    private String authUserNotFoundMessage;

    @Value("${user.role.alreadyExists}")
    private String userRoleAlreadyExistsMessage;

    @Value("${user.role.atLeastOne}")
    private String userAtLeastOneRoleMessage;

    @Value("${user.role.notFound}")
    private String userRoleNotFoundMessage;

    @Override
    public int save(UserDto newDto) {
        log.info("Saving UserDto: {}", newDto);
        assertNotExists(() -> getByEmail(newDto.getEmail()), emailAlreadyExistsMessage);
        assertNotExists(() -> getByPhoneNumber(newDto.getPhoneNumber()), phoneNumberAlreadyExistsMessage);

        User newUser = mapper.map(newDto, User.class);

        newUser.setEnabled(true);
        newUser.setCreationDate(LocalDateTime.now());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        newUser.setAuthProvider(AuthProvider.LOCAL);

        Set<Role> roles = new HashSet<>();
        roles.add(roleDao.getByName("USER"));
        newUser.setRoles(roles);

        newUser = userDao.save(newUser);

        return newUser.getId();
    }

    @Override
    public void delete(int id) {
        log.info("Deleting User by id: {}", id);
        User user = getUserById(id);
        log.debug("User to delete: {}", user);

        slsaClient.deleteCommentByUserId(id);

        String message = "Your account has been deleted";
        String name = "User";
        if (user.getName() != null) {
            name = user.getName();
        }
//        mailClient.sendEmail(name, user.getEmail(), message);
        if (user.getRoles().stream()
                .anyMatch(r -> r.getName().equalsIgnoreCase("COACH"))) {
            try {
                int coachId = slsaClient.getCoachByUserId(user.getId()).getId();
                slsaClient.delete(coachId);
            } catch (FeignException.NotFound ignore) {
            }
        }

        statsService.deleteUserStats(id);

        userDao.delete(user);
    }

    @Override
    public UserDto update(UserDto editedUserDto, int id) {
        log.info("Updating UserDto: {}, id: {}", editedUserDto, id);
        User oldUser = getUserById(id);
        log.debug("Old User: {}", oldUser);

        if (!oldUser.getEmail().equals(editedUserDto.getEmail())) {
            assertNotExists(() -> getByEmail(editedUserDto.getEmail()), emailAlreadyExistsMessage);
        }
        if ((oldUser.getPhoneNumber() != null
                || editedUserDto.getPhoneNumber() != null)
                && !oldUser.getPhoneNumber().equals(editedUserDto.getPhoneNumber())) {
            assertNotExists(() -> getByPhoneNumber(editedUserDto.getPhoneNumber()), phoneNumberAlreadyExistsMessage);
        }

        String oldPassword = oldUser.getPassword();
        mapper.map(editedUserDto, oldUser);

        if (!editedUserDto.getPassword().equals(oldPassword)
                && !passwordEncoder.matches(editedUserDto.getPassword(), oldPassword)) {
            oldUser.setPassword(passwordEncoder.encode(editedUserDto.getPassword()));
        } else {
            oldUser.setPassword(oldPassword);
        }

        return mapper.map(userDao.update(oldUser), UserDto.class);
    }

    @Override
    public MainUserDto getById(int id) {
        log.debug("Mapping User, to MainUserDto. User id: {}", id);
        return mapper.map(getUserById(id), MainUserDto.class);
    }

    @Override
    public MainUserDto getByEmail(String email) {
        try {
            return mapper.map(userDao.getByEmail(email), MainUserDto.class);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(userNotFoundByEmailMessage + email);
        }
    }

    @Override
    public MainUserDto getByPhoneNumber(String phoneNumber) {
        try {
            return mapper.map(userDao.getByPhoneNumber(phoneNumber), MainUserDto.class);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(userNotFoundByPhoneNumberMessage + phoneNumber);
        }
    }

    @Override
    public List<MainUserDto> getAll() {
        log.info("Getting list of MainUserDto");
        return userDao.getAll()
                .stream()
                .map(u -> mapper.map(u, MainUserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public MainUserDto saveImageByUserId(MultipartFile image, int userId) {
        log.info("Getting User by id: {}", userId);
        User user = userDao.getById(userId);
        if (user == null) {
            log.error("User wasn't found. id: {}", userId);
            throw new EntityNotFoundException(userNotFoundByIdMessage + userId);
        } else if (!(user.getPhoto() == null)) {
            log.error("The photo is already tied to the user with id: {}", userId);
            throw new PhotoException(photoTiedMessage + userId);
        }
        log.info("Saving photo to S3");
        String imageUrl = manager.saveImageById(image, userId, "user");
        log.info("Updating user with id: {}", userId);
        user.setPhoto(imageUrl);
        userDao.update(user);
        return mapper.map(user, MainUserDto.class);
    }

    @Override
    public MainUserDto saveImageByUserId(String url, int userId) {
        log.info("Getting User by id: {}", userId);
        User user = userDao.getById(userId);
        if (user == null) {
            log.error("User wasn't found. id: {}", userId);
            throw new EntityNotFoundException(userNotFoundByIdMessage + userId);
        } else if (!(user.getPhoto() == null)) {
            log.error("The photo is already tied to the user with id: {}", userId);
            throw new PhotoException(photoTiedMessage + userId);
        }
        log.info("Updating user with id: {}", userId);
        user.setPhoto(url);
        userDao.update(user);
        return mapper.map(user, MainUserDto.class);
    }

    @Override
    public void deleteImageByUserId(int userId) {
        log.info("Getting User by id: {}", userId);
        User user = userDao.getById(userId);
        if (user == null) {
            log.error("User wasn't found. id: {}", userId);
            throw new EntityNotFoundException(userNotFoundByIdMessage + userId);
        }
        manager.deleteFileByUserId(userId, "user");
        log.info("Updating user with id: {}", userId);
        user.setPhoto(null);
        userDao.update(user);
        mapper.map(user, MainUserDto.class);
    }

    @Override
    public MainUserDto updateImageByUserId(MultipartFile multipartFile, int userId) {
        log.info("Updating User by id: {}", userId);
        User user = userDao.getById(userId);
        if (user == null) {
            log.error("User wasn't found. id: {}", userId);
            throw new EntityNotFoundException(userNotFoundByIdMessage + userId);
        } else if (user.getPhoto() == null) {
            log.error("The photo isn't tied to the user with id: {}", userId);
            throw new PhotoException(photoNotTiedMessage + userId);
        }
        String newUrl = manager.saveImageById(multipartFile, userId, "user");
        log.info("Updating user with id: {}", userId);
        user.setPhoto(newUrl);
        userDao.update(user);
        return mapper.map(user, MainUserDto.class);
    }

    @Override
    public MainUserDto updateImageByUserId(String url, int userId) {
        log.info("Updating User by id: {}", userId);
        User user = userDao.getById(userId);
        if (user == null) {
            log.error("User wasn't found. id: {}", userId);
            throw new EntityNotFoundException(userNotFoundByIdMessage + userId);
        } else if (user.getPhoto() == null) {
            log.error("The photo isn't tied to the user with id: {}", userId);
            throw new PhotoException(photoNotTiedMessage + userId);
        }
        manager.deleteFileByUserId(userId, "user");
        log.info("Updating user with id: {}", userId);
        user.setPhoto(url);
        userDao.update(user);
        return mapper.map(user, MainUserDto.class);
    }

    @Override
    public MainUserDto currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return mapper.map(((UserPrincipal) principal).getUser(), MainUserDto.class);
        } else {
            throw new EntityNotFoundException(authUserNotFoundMessage);
        }
    }

    @Override
    public RoleDto setUserRole(RoleDto role, int userId) {
        User user = getUserById(userId);

        Role newRole = mapper.map(role, Role.class);
        if (user.getRoles().contains(newRole)) {
            throw new EntityRoleException(userRoleAlreadyExistsMessage);
        }

        user.getRoles().add(newRole);
        userDao.update(user);
        return role;
    }

    @Override
    public void deleteUserRole(RoleDto role, int userId) {
        User user = getUserById(userId);

        Role deletedRole = mapper.map(role, Role.class);
        if (!user.getRoles().contains(deletedRole)) {
            throw new EntityRoleException(userRoleNotFoundMessage);
        }
        if (user.getRoles().size() <= 1) {
            throw new EntityRoleException(userAtLeastOneRoleMessage);
        }

        user.getRoles().remove(deletedRole);
        userDao.update(user);
    }

    @Override
    public void online() {
        MainUserDto mainUserDto = currentUser();
        User user = getUserById(mainUserDto.getId());
        if (!user.isOnline()) {
            user.setOnline(true);
        }
        user.setLastSeen(LocalDateTime.now());
        userDao.update(user);
    }

    @Override
    public void offline() {
        MainUserDto mainUserDto = currentUser();
        User user = getUserById(mainUserDto.getId());
        if (user.isOnline()) {
            user.setOnline(false);
            userDao.update(user);
        }
    }

    private User getUserById(int id) {
        log.info("Getting User by id: {}", id);
        User user = userDao.getById(id);
        if (user == null) {
            log.error("User wasn't found. id: {}", id);
            throw new EntityNotFoundException(userNotFoundByIdMessage + id);
        }
        log.info("Found User: {}", user);
        return user;
    }

    private void assertNotExists(Supplier<MainUserDto> supplier, String message) {
        try {
            MainUserDto user = supplier.get();
            if (user != null) {
                throw new EntityAlreadyExistsException(message);
            }
        } catch (EntityNotFoundException ignored) {
        }
    }

}