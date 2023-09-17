package com.russozaripov.springsecurityjwttoken.DTO;

import lombok.Data;

@Data
public class JwtRequestDTO {
    private String username;
    private String password;
}
