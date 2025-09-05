package com.project.springbootbasicwithpostgresql.Controller;

import com.project.springbootbasicwithpostgresql.DTO.LogInRequest;
import com.project.springbootbasicwithpostgresql.DTO.LogInResponse;
import com.project.springbootbasicwithpostgresql.DTO.UserDto;
import com.project.springbootbasicwithpostgresql.Service.CustomUserDetailService;
import com.project.springbootbasicwithpostgresql.Service.CustomUserDetails;
import com.project.springbootbasicwithpostgresql.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;

        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> LogIn(@RequestBody LogInRequest logInRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(logInRequest.getUserName(), logInRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password", e);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(role -> role.getAuthority()).toList();
        LogInResponse response = new LogInResponse(userDetails.getUsername(), roles);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> RegisterUser(@Valid @RequestBody UserDto userDto){
        UserDto savedUser = userService.saveUserToDB(userDto);
        return new ResponseEntity<>(savedUser,HttpStatus.CREATED);
    }



}
