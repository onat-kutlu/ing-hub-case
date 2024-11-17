package com.ing.inghub.model;

import com.ing.inghub.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "CUSTOMER")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "CREDIT_LIMIT")
    private Long creditLimit;

    @Column(name = "USED_CREDIT_LIMIT")
    private Long usedCreditLimit;

    @Column(name = "ROLE")
    private String role = UserRole.CUSTOMER.name();
}
