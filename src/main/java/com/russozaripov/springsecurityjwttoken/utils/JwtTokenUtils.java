package com.russozaripov.springsecurityjwttoken.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secretJwt;
    @Value("${jwt.lifetime}")
    private Duration jwtLifeTime;



    //  к нам приходит пользователь после регистрации и мы выдаем ему токен
    public String generateToken(UserDetails userDetails){
        System.out.println("generate Token");
        Map<String, Object> claims = new HashMap<>();
        // установим в токен роль или роли пользователя
        List<String> rolesList = userDetails.getAuthorities().stream()
                // мы достаем из ползователя список с его ролями, список возвращает объекты Authority
                // для того что бы вкорячить в токен его роли, мы преобразуем этот оюъект в строку
                .map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());
        claims.put("roles", rolesList); //будут значения в нашем токене roles: ADMIN USER и тд
        // с помощью MAP мы можем положить в токен все что угодно
        // пример: claims.put("emails", emailList); и тд
//        claims.put("Test", "Пошео на хуй"); // вшить в токен можно все что угодно, данные отображаются в виде Map<>

        Date issuedData = new Date();// время создания токена
        Date ExpirationDate = new Date(issuedData.getTime() + jwtLifeTime.toMillis()); // время когда истекает токен
        return Jwts.builder()
                // payload token layer
                .setClaims(claims) // roles
                .setSubject(userDetails.getUsername()) // usually in this field set username
                .setIssuedAt(issuedData)
                .setExpiration(ExpirationDate)
                // signature layer
                .signWith(SignatureAlgorithm.HS256, secretJwt)
                .compact();
    }

    public List<String> getUserRoles(String token){
       return getClaimsToken(token).get("roles", List.class);
    }
    public String getUserName(String token){
        return getClaimsToken(token).getSubject();
    }
    // метод который провеярет токен на валидность
    // return type Claim -> is a Map<> username:some username, roles:some roles ....
    public Claims getClaimsToken(String token){
        return Jwts.parser()
                .setSigningKey(secretJwt)
                .parseClaimsJws(token)// здесь идет проверка валидности подписи, срока действия и тд, сама библиотека проверяет эти данные
                .getBody();
    }
}
