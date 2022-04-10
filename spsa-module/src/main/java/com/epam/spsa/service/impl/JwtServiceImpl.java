package com.epam.spsa.service.impl;

import com.epam.spsa.model.User;
import com.epam.spsa.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Data
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.key}")
    private String secretKey;

    @Value("${jwt.token.default-expire-date}")
    private long defaultExpireDate;

    @Value("${jwt.token.incorrect}")
    private String incorrectTokenExceptionMessage;

    @Override
    public String createToken(User user) {
        return createToken(user, new HashMap<>());
    }

    @Override
    public String createToken(User user, long expireDate) {
        return createToken(user, new HashMap<>(), expireDate);
    }

    @Override
    public String createToken(User user, Map<String, Object> claims) {
        return createToken(user, claims, defaultExpireDate);
    }

    @Override
    public String createToken(User user, Map<String, Object> claims, long expireDate) {
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireDate))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        log.info("Created token: {}", token);

        return token;
    }

    @Override
    public String getSubject(String token) {
        try {
            return getClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new JwtException(incorrectTokenExceptionMessage);
        }
    }

    @Override
    public <T> T getClaim(String token, Function<Claims, T> resolver) {
        Claims claims = getAllClaims(token);
        return resolver.apply(claims);
    }

    @Override
    public <T> T getClaim(String token, String name) {
        Claims claims = getAllClaims(token);
        return (T) claims.get(name);
    }

    @Override
    public boolean validateToken(String token) {
        log.info("Validating token: {}", token);
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

}
