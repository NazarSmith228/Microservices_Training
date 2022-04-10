package com.epam.spsa.controller;

import com.epam.spsa.dto.security.RoleDto;
import com.epam.spsa.dto.user.MainUserDto;
import com.epam.spsa.dto.user.UserDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.error.exception.PhotoException;
import com.epam.spsa.service.UserService;
import com.epam.spsa.validation.Image;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
@Validated
@Api(tags = "User")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ApiOperation(value = "Add new user")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "New user created", response = Map.class),
            @ApiResponse(code = 400, message = "Validation error", response = ApiError.class)
    })
    public Map<String, Integer> createUser(@Valid @RequestBody UserDto userDto) {
        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("id", userService.save(userDto));
        return resultMap;
    }

    @GetMapping
    @ApiOperation(value = "View a list of all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all users", response = MainUserDto.class)
    })
    public List<MainUserDto> getUsers() {
        return userService.getAll();
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "View an user by user id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found", response = MainUserDto.class),
            @ApiResponse(code = 404, message = "Non-existing user id", response = ApiError.class)
    })
    public MainUserDto getUserById(@PathVariable int id) {
        return userService.getById(id);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "Edit an user by user id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found and updated", response = UserDto.class),
            @ApiResponse(code = 400, message = "Invalid value(s) for update", response = ApiError.class),
            @ApiResponse(code = 404, message = "Non-existing user id", response = ApiError.class)

    })
    public UserDto updateUser(@Valid @RequestBody UserDto editedUserDto, @PathVariable int id) {
        return userService.update(editedUserDto, id);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete user by user id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found and deleted"),
            @ApiResponse(code = 404, message = "Non-existing user id", response = ApiError.class)
    })
    public void deleteUser(@PathVariable int id) {
        userService.delete(id);
    }

    @PostMapping(value = "/{id}/image")
    @ApiOperation(value = "Save user's image file by user id", response = MainUserDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User found and attached to the photo"),
            @ApiResponse(code = 400, message = "The photo is already tied to the user", response = PhotoException.class),
            @ApiResponse(code = 404, message = "Non-existing user id", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public MainUserDto saveImageByUserId(@RequestParam("file") @Image MultipartFile image, @PathVariable int id) {
        return userService.saveImageByUserId(image, id);
    }

    @PostMapping(value = "/{id}/image-url")
    @ApiOperation(value = "Save user's URL of image by user id", response = MainUserDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User found and attached to the photo"),
            @ApiResponse(code = 400, message = "The photo is already tied to the user", response = PhotoException.class),
            @ApiResponse(code = 404, message = "Non-existing user id", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public MainUserDto saveUrlImageByUserId(@RequestParam String url, @PathVariable int id) {
        return userService.saveImageByUserId(url, id);
    }

    @DeleteMapping(value = "/{id}/image")
    @ApiOperation(value = "Delete user's image by user id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User's photo deleted"),
            @ApiResponse(code = 404, message = "Non-existing user id", response = ApiError.class)
    })
    public void deleteImageByUserId(@PathVariable int id) {
        userService.deleteImageByUserId(id);
    }

    @PutMapping(value = "/{id}/image")
    @ApiOperation(value = "Update user's image by user id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User's photo deleted"),
            @ApiResponse(code = 404, message = "Non-existing user id", response = ApiError.class)
    })
    public MainUserDto updateImageByUserId(@RequestParam("file") @Image MultipartFile image, @PathVariable int id) {
        return userService.updateImageByUserId(image, id);
    }

    @PutMapping(value = "/{id}/url-image")
    @ApiOperation(value = "Update user's url-image by user id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User's photo deleted"),
            @ApiResponse(code = 404, message = "Non-existing user id", response = ApiError.class)
    })
    public MainUserDto updateImageByUserId(@RequestParam String url, @PathVariable int id) {
        return userService.updateImageByUserId(url, id);
    }

    @PostMapping(value = "/{id}/set/role")
    @ApiOperation(value = "Set user role")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Role have been successfully set"),
            @ApiResponse(code = 404, message = "Non-existing user id", response = ApiError.class),
            @ApiResponse(code = 400, message = "Role already exists", response = ApiError.class)
    })
    public RoleDto setUserRole(@RequestBody @Valid RoleDto role,
                               @PathVariable int id) {
        return userService.setUserRole(role, id);
    }

    @DeleteMapping(value = "/{id}/delete/role")
    @ApiOperation(value = "Delete user role")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Role have been successfully deleted"),
            @ApiResponse(code = 404, message = "Non-existing user id", response = ApiError.class),
            @ApiResponse(code = 400, message = "User doesn't have specified role", response = ApiError.class),
            @ApiResponse(code = 400, message = "User must have at least one role", response = ApiError.class)
    })
    public void deleteUserRole(@RequestBody @Valid RoleDto role,
                               @PathVariable int id) {
        userService.deleteUserRole(role, id);
    }

    @GetMapping(value = "/me")
    @ApiOperation(value = "Returns current signed in user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found"),
            @ApiResponse(code = 404, message = "This endpoint works only if the user is authenticated")
    })
    public MainUserDto me() {
        return userService.currentUser();
    }

    @GetMapping(value = "/me/status/online")
    @ApiOperation(value = "The status is set to online first, then upon further requests last seen date is updated")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "This endpoint works only if the user is authenticated")
    })
    public void online() {
        userService.online();
    }

    @GetMapping(value = "/me/status/offline")
    @ApiOperation(value = "Sets status to offline")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "This endpoint works only if the user is authenticated")
    })
    public void offline() {
        userService.offline();
    }


}