package com.russozaripov.springsecurityjwttoken.jwtFilter;

import com.russozaripov.springsecurityjwttoken.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtils tokenUtils;


    // данный метод после каждого запроса с клиента будет проверять токен, доставать оттуда данные о клиенте и каждый раз помещать в security context
    // после каждого запроса мы проверяем заголовок запроса на наличие там токена
    // Headers -> Authorization -> Bearer <any Token>
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");// токен находится в заголовке запроса с ключом Authorization
        String jwt = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            jwt = authHeader.substring(7); // достаем конкретно токен из заголовка, он имеет вид (Bearer :) поэтому первые 7 символов пропускаем и достаем все что идет после Bearer
            try {

            username = tokenUtils.getUserName(jwt); // достаем имя пользователя которое мы вшили в payload токена
            }catch (ExpiredJwtException expiredJwtException){
                log.debug("Время жизни токена истекло");
            }catch (SignatureException signatureException){
                log.debug("Неверная подпись в токене");
            }
        }
        // при каждом rest запросе с клиента мы не обращаемся в базу данных, а собираем данные о клиенте прямо из токена JWT
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    tokenUtils.getUserRoles(jwt).stream().map(roles -> new SimpleGrantedAuthority(roles)).collect(Collectors.toList()) // сюда мы должны передать Collection<GrantedAuthority> и поэтому нужно строку конвертирвоать
            );
            SecurityContextHolder.getContext().setAuthentication(token); // кладем в security Context данные о пользователи
        }
            filterChain.doFilter(request, response);// данный метод говорит что надо продолжить движение по фильтрам дальше к искомой точке


    }
}
