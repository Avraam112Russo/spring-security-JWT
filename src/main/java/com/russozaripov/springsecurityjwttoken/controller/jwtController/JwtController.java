package com.russozaripov.springsecurityjwttoken.controller.jwtController;

import com.russozaripov.springsecurityjwttoken.DTO.JwtRequestDTO;
import com.russozaripov.springsecurityjwttoken.DTO.JwtResponseDTO;
import com.russozaripov.springsecurityjwttoken.exception.BadCredentialsDTO;
import com.russozaripov.springsecurityjwttoken.service.UserService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jwt")
public class JwtController {
    @Autowired
    private AuthenticationManager authenticationManager; // данный объект занимается проверкой пользователя и пароля
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtils tokenUtils;
    @PostMapping("/auth")
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
    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
