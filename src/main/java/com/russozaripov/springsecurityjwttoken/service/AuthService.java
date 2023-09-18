package com.russozaripov.springsecurityjwttoken.service;

import com.russozaripov.springsecurityjwttoken.DTO.JwtRequestDTO;
import com.russozaripov.springsecurityjwttoken.DTO.JwtResponseDTO;
import com.russozaripov.springsecurityjwttoken.DTO.RegistrationUserDTO;
import com.russozaripov.springsecurityjwttoken.DTO.USerDTO;
import com.russozaripov.springsecurityjwttoken.entity.User;
import com.russozaripov.springsecurityjwttoken.exception.BadCredentialsDTO;
import com.russozaripov.springsecurityjwttoken.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager; // данный объект занимается проверкой пользователя и пароля
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtils tokenUtils;

    public ResponseEntity<?> get_Jwt_Token(@RequestBody JwtRequestDTO jwtRequestDTO){
        try {
            System.out.println("get_jwt_Controller");
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequestDTO.getUsername(), jwtRequestDTO.getPassword()));
//          System.out.println(authenticate.getPrincipal());
            //authenticationManager.authenticate -> отправляем данные на проверку в userDetails service и наш сервис запршивает данные из базы и сравнивает их
            // если проверка прошла успешно то возвращается объект AUTHENTICATION
        } catch (BadCredentialsException exception){ // если данные не верны то выкидываем ошибку
            return new ResponseEntity<>(new BadCredentialsDTO(HttpStatus.UNAUTHORIZED.value(), "Неверно указан логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(jwtRequestDTO.getUsername());
        String TOKEN = tokenUtils.generateToken(userDetails);
        return ResponseEntity.ok().body(new JwtResponseDTO(TOKEN));
    }

    public ResponseEntity<?> newUserRegistration(@RequestBody RegistrationUserDTO registrationUserDTO){
        if (!registrationUserDTO.getPassword().equals(registrationUserDTO.getConfirmPassword())){
            return new ResponseEntity<>(new BadCredentialsDTO(HttpStatus.BAD_REQUEST.value(), "Введенные пароли не совпадают"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findUserByUserName(registrationUserDTO.getUsername()).isPresent()){
            return new ResponseEntity<>(new BadCredentialsDTO(HttpStatus.BAD_REQUEST.value(), "Пользователь с такими данными уже существует"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDTO);
        return new ResponseEntity<>(new USerDTO(user.getId(), user.getEmail(), user.getUsername()), HttpStatus.OK);
    }
}
