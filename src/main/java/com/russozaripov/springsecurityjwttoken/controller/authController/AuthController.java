package com.russozaripov.springsecurityjwttoken.controller.authController;

import com.russozaripov.springsecurityjwttoken.DTO.JwtRequestDTO;
import com.russozaripov.springsecurityjwttoken.DTO.JwtResponseDTO;
import com.russozaripov.springsecurityjwttoken.DTO.RegistrationUserDTO;
import com.russozaripov.springsecurityjwttoken.DTO.USerDTO;
import com.russozaripov.springsecurityjwttoken.entity.User;
import com.russozaripov.springsecurityjwttoken.exception.BadCredentialsDTO;
import com.russozaripov.springsecurityjwttoken.service.AuthService;
import com.russozaripov.springsecurityjwttoken.service.UserService;
import com.russozaripov.springsecurityjwttoken.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jwt")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<?> get_Jwt_Token(@RequestBody JwtRequestDTO jwtRequestDTO){
        return authService.get_Jwt_Token(jwtRequestDTO);
    }



    @PostMapping("/registration")
    public ResponseEntity<?> newUserRegistration(@RequestBody RegistrationUserDTO registrationUserDTO){
       return authService.newUserRegistration(registrationUserDTO);
    }
}
