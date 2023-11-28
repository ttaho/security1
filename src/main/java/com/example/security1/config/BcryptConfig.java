package com.example.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BcryptConfig {
    @Bean // IoC(제어의 역전)에 등록해준다.
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }
}
