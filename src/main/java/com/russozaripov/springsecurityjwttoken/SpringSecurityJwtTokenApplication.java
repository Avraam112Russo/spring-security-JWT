package com.russozaripov.springsecurityjwttoken;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.SecureRandom;
import java.util.Base64;

@SpringBootApplication
public class SpringSecurityJwtTokenApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtTokenApplication.class, args);
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256 бит
        random.nextBytes(keyBytes);
        String secretKey = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Generated Secret Key: " + secretKey);
    }

}
