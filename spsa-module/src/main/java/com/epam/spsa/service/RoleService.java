package com.epam.spsa.service;

import com.epam.spsa.dto.security.RoleDto;

import java.util.List;

public interface RoleService {

    RoleDto getById(int id);

    RoleDto getByName(String name);

    List<RoleDto> getAll();

}
