package com.epam.spsa.dao;

import com.epam.spsa.model.User;

public interface UserDao extends MainDao<User> {

    User update(User user);

    void delete(User user);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    User getByProviderId(String providerId);

}
