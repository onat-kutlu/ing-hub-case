package com.ing.inghub.service.security;

import com.ing.inghub.constants.errorcodes.ErrorCodes;
import com.ing.inghub.model.Customer;
import com.ing.inghub.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@SpringBootTest
public class UserDetailsServiceTests {

    @Autowired
    UserDetailsService userDetailsService;

    @MockBean
    CustomerRepository customerRepository;

    @Test
    void loadUserByUsername_when_customerPresent() {
        //given
        Mockito.doReturn(Optional.of(new Customer().setUsername("username").setPassword("password").setRole("CUSTOMER"))).when(customerRepository).findByUsername(Mockito.anyString());
        //when
        UserDetails userDetails = userDetailsService.loadUserByUsername("username");
        //then
        Assertions.assertEquals("username", userDetails.getUsername());
        Assertions.assertEquals("password", userDetails.getPassword());
        Assertions.assertEquals("ROLE_CUSTOMER", userDetails.getAuthorities().iterator().next().getAuthority());
    }


    @Test
    void loadUserByUsername_when_customerNotFound() {
        //given
        Mockito.doReturn(Optional.empty()).when(customerRepository).findByUsername(Mockito.anyString());
        //when
        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class, () ->  userDetailsService.loadUserByUsername("username"));
        //then
        Assertions.assertEquals(ErrorCodes.E_CUSTOMER_NOT_FOUND, exception.getMessage());

    }

}
