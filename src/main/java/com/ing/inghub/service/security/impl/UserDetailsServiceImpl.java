package com.ing.inghub.service.security.impl;

import com.ing.inghub.constants.errorcodes.ErrorCodes;
import com.ing.inghub.model.Customer;
import com.ing.inghub.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CustomerRepository customerRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserDetails(username);
    }

    private UserDetails getUserDetails(String username) {
        return customerRepository.findByUsername(username)
                .map(customer -> from(customer))
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCodes.E_CUSTOMER_NOT_FOUND));
    }

    private UserDetails from(Customer customer) {
        return User.builder()
                .username(customer.getUsername())
                .password(customer.getPassword())
                .roles(customer.getRole())
                .build();
    }
}
