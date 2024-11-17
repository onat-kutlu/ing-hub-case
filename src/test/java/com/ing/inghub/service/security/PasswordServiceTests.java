package com.ing.inghub.service.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordServiceTests {
    @Autowired
    PasswordService passwordService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void encodePassword() {
        //given & whem
        String encodedPassword = passwordService.encodePassword("password");
        //then
        Assertions.assertTrue(passwordEncoder.matches("password", encodedPassword));
    }
}
