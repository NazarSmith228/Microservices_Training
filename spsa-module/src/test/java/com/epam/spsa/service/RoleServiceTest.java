package com.epam.spsa.service;

import com.epam.spsa.dao.RoleDao;
import com.epam.spsa.dto.security.RoleDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.Role;
import com.epam.spsa.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
public class RoleServiceTest {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleDao roleDao;

    @Mock
    private ModelMapper mapper;

    @Test
    public void successfulGetByIdTest() {
        int id = 1;
        Role role = getRole();

        Mockito.when(roleDao.getById(id)).thenReturn(role);
        Mockito.when(mapper.map(role, RoleDto.class)).thenReturn(getRoleDto());

        RoleDto roleDto = roleService.getById(id);

        assertEquals(role.getId(), roleDto.getId());
        assertEquals(role.getName(), roleDto.getName());
    }

    @Test
    public void roleNotFoundByIdTest() {
        Mockito.when(roleDao.getById(anyInt())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> roleService.getById(666));
    }

    @Test
    public void successfulGetByNameTest() {
        String name = "COACH";
        Role role = getRole();

        Mockito.when(roleDao.getByName(name)).thenReturn(role);
        Mockito.when(mapper.map(role, RoleDto.class)).thenReturn(getRoleDto());

        RoleDto roleDto = roleService.getByName(name);

        assertEquals(role.getId(), roleDto.getId());
        assertEquals(role.getName(), roleDto.getName());
    }

    @Test
    public void roleNotFoundByNameTest() {
        Mockito.when(roleDao.getByName(anyString())).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> roleService.getByName("COACH"));
    }

    private Role getRole() {
        return Role.builder()
                .id(1)
                .name("COACH")
                .build();
    }

    private RoleDto getRoleDto() {
        return RoleDto.builder()
                .id(1)
                .name("COACH")
                .build();
    }

}
