package com.ing.inghub.service.auth.impl;

import com.ing.inghub.constants.errorcodes.ErrorCodes;
import com.ing.inghub.dto.CustomerDto;
import com.ing.inghub.dto.auth.LoginRequest;
import com.ing.inghub.dto.auth.LoginResponse;
import com.ing.inghub.dto.auth.RegisterRequest;
import com.ing.inghub.dto.auth.RegisterResponse;
import com.ing.inghub.dto.base.ServiceResult;
import com.ing.inghub.exception.IngException;
import com.ing.inghub.model.Customer;
import com.ing.inghub.service.auth.AuthService;
import com.ing.inghub.service.customer.CustomerService;
import com.ing.inghub.service.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public RegisterResponse register(final RegisterRequest registerRequest) throws IngException {
        checkCustomerExist(registerRequest.getUsername());
        CustomerDto customerDto = customerService.createCustomer(registerRequest);
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setCustomerDto(customerDto);
        registerResponse.setServiceResult(ServiceResult.getSuccessResult());
        return registerResponse;
    }

    @Override
    public LoginResponse login(final LoginRequest loginRequest) throws IngException {
        Customer customer = customerService.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new IngException(ErrorCodes.E_CUSTOMER_NOT_FOUND));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        String token = jwtTokenProvider.generateToken(customer.getUsername());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setTokenExpirationDate(jwtTokenProvider.getExpirationDate(token));
        loginResponse.setServiceResult(ServiceResult.getSuccessResult());
        return loginResponse;

    }


    private void checkCustomerExist(String username) throws IngException {
        if (customerService.findByUsername(username).isPresent()) {
            throw new IngException(ErrorCodes.E_CUSTOMER_ALREADY_EXIST);
        }
    }


}
