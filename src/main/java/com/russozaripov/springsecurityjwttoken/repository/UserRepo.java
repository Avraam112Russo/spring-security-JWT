package com.russozaripov.springsecurityjwttoken.repository;

import com.russozaripov.springsecurityjwttoken.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
}
