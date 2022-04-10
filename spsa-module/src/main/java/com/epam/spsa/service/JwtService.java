package com.epam.spsa.service;

import com.epam.spsa.model.User;
import io.jsonwebtoken.Claims;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String createToken(User user);

    String createToken(User user, long expireDate);

    String createToken(User user, Map<String, Object> claims);

    String createToken(User user, Map<String, Object> claims, long expireDate);

    String getSubject(String token);

    <T> T getClaim(String token, Function<Claims, T> resolver);

    <T> T getClaim(String token, String name);

    boolean validateToken(String token);

}
