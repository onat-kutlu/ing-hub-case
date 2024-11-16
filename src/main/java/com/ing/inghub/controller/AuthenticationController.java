package com.ing.inghub.controller;

import com.ing.inghub.dto.auth.LoginRequest;
import com.ing.inghub.dto.auth.LoginResponse;
import com.ing.inghub.dto.auth.RegisterRequest;
import com.ing.inghub.dto.auth.RegisterResponse;
import com.ing.inghub.exception.IngException;
import com.ing.inghub.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/auth")
@Tag(name = "Authentication Api", description = "Authentication Api")
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(method = "POST", summary = "Register", description = "Register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody final RegisterRequest request)
            throws IngException {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/login")
    @Operation(method = "POST", summary = "Login", description = "Login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody final LoginRequest request)
            throws IngException {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok().body(response);
    }
}
