package com.ing.inghub.mapper;


import com.ing.inghub.dto.CustomerDto;
import com.ing.inghub.dto.auth.RegisterRequest;
import com.ing.inghub.model.Customer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomerMapper {

    public static Customer from(RegisterRequest registerRequest) {
        return new Customer()
                .setName(registerRequest.getName())
                .setUsername(registerRequest.getUsername())
                .setSurname(registerRequest.getSurname())
                .setUsedCreditLimit(0L);
    }

    public static CustomerDto from(Customer customer) {
        return new CustomerDto()
                .setId(customer.getId())
                .setName(customer.getName())
                .setSurname(customer.getSurname())
                .setUsername(customer.getUsername())
                .setPassword(customer.getPassword())
                .setCreditLimit(customer.getCreditLimit())
                .setUsedCreditLimit(customer.getUsedCreditLimit())
                .setStatus(customer.getRole());
    }

}
