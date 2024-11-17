package com.ing.inghub.service.auth;

import com.ing.inghub.constants.errorcodes.ErrorCodes;
import com.ing.inghub.dto.auth.LoginRequest;
import com.ing.inghub.dto.auth.LoginResponse;
import com.ing.inghub.dto.auth.RegisterRequest;
import com.ing.inghub.dto.auth.RegisterResponse;
import com.ing.inghub.exception.IngException;
import com.ing.inghub.model.Customer;
import com.ing.inghub.service.customer.CustomerService;
import com.ing.inghub.service.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTests {

    @Autowired
    AuthService authService;
    @MockBean
    CustomerService customerService;
    @MockBean
    AuthenticationManager authenticationManager;
    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @Test
    void register_when_customerDontExist() throws IngException {
        //given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        registerRequest.setName("onat");
        registerRequest.setSurname("kutlu");
        Mockito.doReturn(Optional.empty()).when(customerService).findByUsername(registerRequest.getUsername());
        //when
        RegisterResponse registerResponse = authService.register(registerRequest);
        //then
        Assertions.assertNotNull(registerResponse);
        Assertions.assertNotNull(registerResponse.getServiceResult());
        Assertions.assertEquals(ErrorCodes.SUCCESS_MESSAGE, registerResponse.getServiceResult().getMessage());
        Assertions.assertEquals(ErrorCodes.SUCCESS_CODE, registerResponse.getServiceResult().getCode());
    }

    @Test
    void register_when_customerExists() {
        //given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        registerRequest.setName("onat");
        registerRequest.setSurname("kutlu");
        Mockito.doReturn(Optional.of(new Customer())).when(customerService).findByUsername(registerRequest.getUsername());
        //when
        IngException exception = Assertions.assertThrows(IngException.class, () -> authService.register(registerRequest));
        //then
        Assertions.assertEquals(ErrorCodes.E_CUSTOMER_ALREADY_EXIST, exception.getCode());

    }

    @Test
    void login_when_customerDontExists() {
        //given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");
        Mockito.doReturn(Optional.empty()).when(customerService).findByUsername(Mockito.anyString());
        //when
        IngException exception = Assertions.assertThrows(IngException.class, () -> authService.login(loginRequest));
        //then
        Assertions.assertEquals(ErrorCodes.E_CUSTOMER_NOT_FOUND, exception.getCode());

    }

    @Test
    void login_when_passwordCorrect() throws IngException {
        //given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");
        Mockito.doReturn(Optional.of(new Customer().setUsername("username"))).when(customerService).findByUsername(Mockito.anyString());
        Authentication mockAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        Mockito.doReturn("token").when(jwtTokenProvider).generateToken(Mockito.anyString());
        //when
        LoginResponse response = authService.login(loginRequest);
        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(ErrorCodes.SUCCESS_MESSAGE, response.getServiceResult().getMessage());
        Assertions.assertEquals(ErrorCodes.SUCCESS_CODE, response.getServiceResult().getCode());
        Assertions.assertEquals("token", response.getToken());

    }


}
