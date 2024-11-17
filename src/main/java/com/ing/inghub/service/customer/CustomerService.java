package com.ing.inghub.service.customer;

import com.ing.inghub.dto.CustomerDto;
import com.ing.inghub.dto.auth.RegisterRequest;
import com.ing.inghub.model.Customer;

import java.util.Optional;

public interface CustomerService {

    Optional<Customer> findByUsername(String username);

    CustomerDto createCustomer(RegisterRequest registerRequest);

    Optional<Customer> findByCustomerId(Long customerId);
}
