package com.project.springbootbasicwithpostgresql.Controller;

import com.project.springbootbasicwithpostgresql.DTO.UserDto;
import com.project.springbootbasicwithpostgresql.Model.Users;
import com.project.springbootbasicwithpostgresql.Repository.UserRepository;
import com.project.springbootbasicwithpostgresql.Service.CustomUserDetailService;
import com.project.springbootbasicwithpostgresql.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AppController {

    private final UserService userService;

    private final UserRepository userRepository;

    public AppController( UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
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
    public String createUser(@Valid @RequestBody UserDto userDto){
        userService.saveUserToDB(userDto);
        return "User created successfully";
    }

    @GetMapping("/list")
    public ResponseEntity<List<Users>> getAllUser(){
        List<Users> usersList = userRepository.findAll();
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }
}
