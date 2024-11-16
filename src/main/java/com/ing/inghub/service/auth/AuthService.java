package com.ing.inghub.service.auth;

import com.ing.inghub.dto.auth.LoginRequest;
import com.ing.inghub.dto.auth.LoginResponse;
import com.ing.inghub.dto.auth.RegisterRequest;
import com.ing.inghub.dto.auth.RegisterResponse;
import com.ing.inghub.exception.IngException;

public interface AuthService {

    RegisterResponse register(final RegisterRequest registerRequest) throws IngException;

    LoginResponse login(final LoginRequest registerRequest) throws IngException;
}
