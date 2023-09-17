package com.russozaripov.springsecurityjwttoken.repository;

import com.russozaripov.springsecurityjwttoken.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepo extends JpaRepository<Roles, Integer> {
    Optional<Roles>findRolesByName(String name);
}
