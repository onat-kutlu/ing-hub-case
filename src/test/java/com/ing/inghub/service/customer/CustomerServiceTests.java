package com.ing.inghub.service.customer;

import com.ing.inghub.dto.auth.RegisterRequest;
import com.ing.inghub.model.Customer;
import com.ing.inghub.repository.CustomerRepository;
import com.ing.inghub.service.security.PasswordService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class CustomerServiceTests {

    @Autowired
    CustomerService customerService;

    @MockBean
    PasswordService passwordService;

    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    void beforeEach() {
        customerRepository.deleteAll();
    }

    @Test
    void findByUsername_whenCustomerExist() {
        //given
        Customer customerToBeSaved = createCustomer();
        customerRepository.save(customerToBeSaved);
        // when
        Optional<Customer> customer = customerService.findByUsername("onatk");
        //then
        Assertions.assertTrue(customer.isPresent());
    }

    @Test
    void findByUsername_whenCustomerDontExist() {
        //given
        Customer customerToBeSaved = createCustomer();
        customerRepository.save(customerToBeSaved);
        // when
        Optional<Customer> customer = customerService.findByUsername("ingAdmin");
        //then
        Assertions.assertFalse(customer.isPresent());
    }

    @Test
    void createCustomerTest() {
        //given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("ingAdmin");
        registerRequest.setPassword("112233");
        registerRequest.setName("ing");
        registerRequest.setSurname("admin");
        Mockito.doReturn("hashedPassword").when(passwordService).encodePassword(Mockito.anyString());
        //when
        customerService.createCustomer(registerRequest);
        //then
        Optional<Customer> customer = customerRepository.findByUsername("ingAdmin");
        Assertions.assertTrue(customer.isPresent());
        Assertions.assertEquals(registerRequest.getUsername(), customer.get().getUsername());
        Assertions.assertEquals("hashedPassword", customer.get().getPassword());
        Assertions.assertEquals(registerRequest.getName(), customer.get().getName());
        Assertions.assertEquals(registerRequest.getSurname(), customer.get().getSurname());

    }

    @Test
    void findByCustomerId_whenCustomerExist() {
        //given
        Customer customerToBeSaved = createCustomer();
        customerToBeSaved = customerRepository.save(customerToBeSaved);
        // when
        Optional<Customer> customer = customerService.findByCustomerId(customerToBeSaved.getId());
        //then
        Assertions.assertTrue(customer.isPresent());
        Assertions.assertEquals(customerToBeSaved.getId(), customer.get().getId());
    }

    @Test
    void findByCustomerId_whenCustomerDontExist() {
        //given && when
        Optional<Customer> customer = customerService.findByCustomerId(1L);
        //then
        Assertions.assertFalse(customer.isPresent());
    }

    @Test
    void saveCustomerTest() {
        //given
        Customer customerToBeSaved = createCustomer();
        //when
        Customer customer = customerService.save(customerToBeSaved);
        //then
        Assertions.assertEquals(customerToBeSaved.getId(), customer.getId());
        Assertions.assertEquals(customerToBeSaved.getName(), customer.getName());
        Assertions.assertEquals(customerToBeSaved.getSurname(), customer.getSurname());
        Assertions.assertEquals(customerToBeSaved.getUsername(), customer.getUsername());
        Assertions.assertEquals(customerToBeSaved.getPassword(), customer.getPassword());


    }


    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setName("onat");
        customer.setSurname("kutlu");
        customer.setUsername("onatk");
        customer.setPassword("$2a$10$h7lptqfWNlf8s1d9mniVseNQzSng.ob5jOd0lntbK/nXBC5M4xLvu");
        return customer;
    }
}
