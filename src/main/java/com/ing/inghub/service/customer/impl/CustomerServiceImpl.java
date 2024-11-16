package com.ing.inghub.service.customer.impl;

import com.ing.inghub.constants.constants.Constants;
import com.ing.inghub.dto.CustomerDto;
import com.ing.inghub.dto.auth.RegisterRequest;
import com.ing.inghub.mapper.CustomerMapper;
import com.ing.inghub.model.Customer;
import com.ing.inghub.repository.CustomerRepository;
import com.ing.inghub.service.customer.CustomerService;
import com.ing.inghub.service.security.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordService passwordService;

    @Override
    public Optional<Customer> findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public CustomerDto createCustomer(RegisterRequest registerRequest) {
        Customer customer = CustomerMapper.from(registerRequest);
        customer.setPassword(passwordService.encodePassword(registerRequest.getPassword()));
        customer.setCreditLimit(Constants.DEFAULT_CREDIT_LIMIT);
        customerRepository.save(customer);
        return CustomerMapper.from(customer);
    }
}
