package com.russozaripov.springsecurityjwttoken.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationUserDTO {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;

}
