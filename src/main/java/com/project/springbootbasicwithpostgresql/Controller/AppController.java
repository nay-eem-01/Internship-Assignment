package com.project.springbootbasicwithpostgresql.Controller;

import com.project.springbootbasicwithpostgresql.DTO.UserDto;
import com.project.springbootbasicwithpostgresql.Model.Users;
import com.project.springbootbasicwithpostgresql.Service.CustomUserDetailService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public AppController(CustomUserDetailService customUserDetailService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.customUserDetailService = customUserDetailService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
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

        Users newUser = modelMapper.map(userDto,Users.class);

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        customUserDetailService.SaveUserToDB(newUser);
        return "User created successfully";
    }
}
