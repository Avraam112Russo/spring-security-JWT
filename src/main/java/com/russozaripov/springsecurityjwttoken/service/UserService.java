package com.russozaripov.springsecurityjwttoken.service;


import com.russozaripov.springsecurityjwttoken.entity.User;
import com.russozaripov.springsecurityjwttoken.repository.RoleRepo;
import com.russozaripov.springsecurityjwttoken.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;

    public Optional<User> findUserByUserName(String username){
        System.out.println("findUserByUserByUserName");
        return userRepo.findUserByUsername(username);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUserName(username).orElseThrow(
                () -> new UsernameNotFoundException("User with username: '%s' not found.".formatted(username)));

        System.out.println("loadByUserName");
        // преобразуем нашего юзера в спрингового юзера
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), user.getRoles()
                .stream().map(roles -> new SimpleGrantedAuthority(roles.getName()))
                .collect(Collectors.toList())
        );
    }
    public void createNewUser(User user){
        user.setRoles(List.of(roleRepo.findRolesByName("ROLE_USER").get()));
        userRepo.save(user);
    }
}
