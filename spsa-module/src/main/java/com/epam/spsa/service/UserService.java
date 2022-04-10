package com.epam.spsa.service;

import com.epam.spsa.dto.security.RoleDto;
import com.epam.spsa.dto.user.MainUserDto;
import com.epam.spsa.dto.user.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    int save(UserDto newDto);

    void delete(int id);

    UserDto update(UserDto updateDto, int id);

    MainUserDto getById(int id);

    MainUserDto getByEmail(String email);

    MainUserDto getByPhoneNumber(String phoneNumber);

    List<MainUserDto> getAll();

    MainUserDto saveImageByUserId(MultipartFile image, int userId);

    MainUserDto saveImageByUserId(String url, int userId);

    void deleteImageByUserId(int userId);

    MainUserDto updateImageByUserId(MultipartFile multipartFile, int userId);

    MainUserDto updateImageByUserId(String url, int userId);

    MainUserDto currentUser();

    RoleDto setUserRole(RoleDto roles, int userId);

    void deleteUserRole(RoleDto roles, int userId);

    void online();

    void offline();

}
