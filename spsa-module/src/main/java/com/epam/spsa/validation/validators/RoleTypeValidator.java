package com.epam.spsa.validation.validators;

import com.epam.spsa.dao.RoleDao;
import com.epam.spsa.dto.security.RoleDto;
import com.epam.spsa.model.Role;
import com.epam.spsa.validation.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class RoleTypeValidator implements ConstraintValidator<RoleType, RoleDto> {

    private final RoleDao roleDao;

    private final ModelMapper mapper;

    @Override
    public boolean isValid(RoleDto role, ConstraintValidatorContext context) {
        log.info("Validating SportType with value: {}", role);
        List<Role> allSportTypes = roleDao.getAll();
        return allSportTypes.contains(mapper.map(role, Role.class));
    }

}
