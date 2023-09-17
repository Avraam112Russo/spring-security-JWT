package com.russozaripov.springsecurityjwttoken.config;

import com.russozaripov.springsecurityjwttoken.jwtFilter.JwtRequestFilter;
import com.russozaripov.springsecurityjwttoken.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity // говорим спрингу что этот класс ответственен за безопасность приложения
@EnableGlobalMethodSecurity(securedEnabled = true)// говорим спрингу что некоторые методы в нашем приложении будут доступны только определенным  ролям
@Configuration
public class Config {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        System.out.println("securityFilterChain");
        httpSecurity
                .csrf().disable()
                .cors().disable()
                .authorizeRequests().antMatchers("/api/v1/secured").authenticated()
                .antMatchers("/api/v1/admin").hasRole("ADMIN")
                .antMatchers("/api/v1/userInfo").authenticated()
                // при попытке зайти в защищенную область, спринг смотрит в security context и проверяет есть ли там пользователь который хочет зайти
                // как раз для этого и нужен токен, при каждом запросе, пользователю что бы попасть в контекст нужно ввести логин и пароль или воспользоваться токеном
                .antMatchers("/api/jwt/auth").permitAll()
                .antMatchers("/api/jwt/test").permitAll()
                .antMatchers("/api/v1/unsecured").permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // теперь сессии будут без состояния и не будут поддерживаться, так как мы подключим jwt токен
                .and()
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        // если у пользователя есть токен, то он будет помещен в спринг контекст и этот фильтр UsernamePasswordAuthenticationFilter его пропустит без логина и пароля
            return httpSecurity.build();

    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        System.out.println("DaoAuthenticationProvider");
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        System.out.println("BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        System.out.println("AuthenticationManager");
        return authenticationConfiguration.getAuthenticationManager();
    }
}
