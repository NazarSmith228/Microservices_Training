package com.epam.spsa.service;

import com.epam.spsa.dto.security.AuthDto;

public interface AuthenticationService {

    String authenticate(AuthDto authDto);

}
