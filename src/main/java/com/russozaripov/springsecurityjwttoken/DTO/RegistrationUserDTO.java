package com.russozaripov.springsecurityjwttoken.DTO;

import lombok.Data;

@Data
public class RegistrationUserDTO {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;

}