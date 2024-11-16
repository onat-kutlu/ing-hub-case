package com.ing.inghub.service.security;

import java.time.Instant;

public interface JwtTokenProvider {

    String generateToken(String username);

    String getUsernameFromToken(String token);

    boolean validateToken(String token,String username);

    Instant getExpirationDate(String token);
}
