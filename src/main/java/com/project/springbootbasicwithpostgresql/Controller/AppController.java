package com.project.springbootbasicwithpostgresql.Controller;

import com.project.springbootbasicwithpostgresql.Model.Users;
import com.project.springbootbasicwithpostgresql.Service.CustomUserDetailService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;

    public AppController(CustomUserDetailService customUserDetailService, PasswordEncoder passwordEncoder) {
        this.customUserDetailService = customUserDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/public")
    public String publicEndpoint(){
        return  "This is a public endpoint";
    }
    @GetMapping("/user")
    public String userEndpoint(){

        return  "This is a user endpoint";
    }
    @GetMapping("/admin")
    public String adminEndpoint(){
        return  "This is an admin endpoint";
    }


    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser(@RequestBody Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Users newUser = customUserDetailService.SaveUserToDB(user);
        return "User created successfully";
    }
}
