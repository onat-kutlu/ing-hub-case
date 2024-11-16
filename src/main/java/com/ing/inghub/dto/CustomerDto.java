package com.ing.inghub.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class CustomerDto {
    private Long id;
    private String name;
    private String surname;
    private String username;
    @JsonIgnore
    private String password;
    private Long creditLimit;
    private Long usedCreditLimit;
    private String status;
}
