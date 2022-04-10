package com.epam.spsa.dao;

import com.epam.spsa.model.Role;

public interface RoleDao extends MainDao<Role> {

    Role getByName(String name);

}
