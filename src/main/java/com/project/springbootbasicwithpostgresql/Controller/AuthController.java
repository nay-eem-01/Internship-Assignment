package com.project.springbootbasicwithpostgresql.Controller;

import com.project.springbootbasicwithpostgresql.DTO.LogInRequest;
import com.project.springbootbasicwithpostgresql.DTO.LogInResponse;
import com.project.springbootbasicwithpostgresql.DTO.UserDto;
import com.project.springbootbasicwithpostgresql.Security.JWT.JwtUtil;
import com.project.springbootbasicwithpostgresql.Service.CustomUserDetails;
import com.project.springbootbasicwithpostgresql.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;


    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;

        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> LogIn(@RequestBody LogInRequest logInRequest, HttpServletRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(logInRequest.getUserName(), logInRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password", e);
        }


        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        LogInResponse response = new LogInResponse(userDetails.getUsername(), roles,jwt);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> RegisterUser(@Valid @RequestBody UserDto userDto){
        UserDto savedUser = userService.saveUserToDB(userDto);
        return new ResponseEntity<>(savedUser,HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("Message","Logged out successfully"));
    }

}
