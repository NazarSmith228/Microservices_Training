package com.epam.spsa.service.impl;

import com.epam.spsa.dao.RoleDao;
import com.epam.spsa.dto.security.RoleDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.Role;
import com.epam.spsa.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    private final ModelMapper mapper;

    @Value("${role.exception.notFoundId}")
    private String roleNotFoundByIdMessage;

    @Value("${role.exception.notFoundName}")
    private String roleNotFoundByNameMessage;

    @Override
    public RoleDto getById(int id) {
        Role role = roleDao.getById(id);
        if (role == null) {
            throw new EntityNotFoundException(roleNotFoundByIdMessage + id);
        }
        return mapper.map(roleDao.getById(id), RoleDto.class);
    }

    @Override
    public RoleDto getByName(String name) {
        try {
            return mapper.map(roleDao.getByName(name.toUpperCase()), RoleDto.class);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(roleNotFoundByNameMessage + name);
        }
    }

    @Override
    public List<RoleDto> getAll() {
        return roleDao.getAll().stream()
                .map(r -> mapper.map(r, RoleDto.class))
                .collect(Collectors.toList());
    }

}
