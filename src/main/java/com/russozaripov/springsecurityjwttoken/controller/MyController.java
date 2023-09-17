package com.russozaripov.springsecurityjwttoken.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class MyController {

    @GetMapping("/unsecured")
    public String unsecuredData(){
        System.out.println("/unsecured");
        return "unsecuredData";
    }

    @GetMapping("/secured")
    public String securedData(){
        return "securedData";
    }

    @GetMapping("/admin")
    public String adminData(){
        return "adminData";
    }
    @GetMapping("/userInfo")
    public String userInfo(Principal principal){
        return principal.getName();
    }
}
