package com.ing.inghub.service.security.impl;

import com.ing.inghub.service.security.JwtTokenProvider;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtTokenProviderImpl implements JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.millisecond:3600000}")
    private long expirationDuration;

    @Override
    public String generateToken(String username) {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationDuration))
                .setSubject(username)
                .signWith(getJwtSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String getUsernameFromToken(String token) {
        return getJwtParser()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public boolean validateToken(String token, String username) {
        try {
            return !isJwtExpired(token) && getJwtParser().parseClaimsJws(token).getBody().getSubject().equals(username);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Instant getExpirationDate(String token) {
        return getJwtParser().parseClaimsJws(token).getBody().getExpiration().toInstant();
    }

    private boolean isJwtExpired(String token) {
        return getJwtParser().parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    private JwtParser getJwtParser() {
        return Jwts.parser()
                .setSigningKey(getJwtSigningKey())
                .build();
    }

    private Key getJwtSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
