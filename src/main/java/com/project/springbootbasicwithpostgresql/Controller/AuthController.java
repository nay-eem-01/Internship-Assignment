package com.project.springbootbasicwithpostgresql.Controller;

import com.project.springbootbasicwithpostgresql.DTO.*;
import com.project.springbootbasicwithpostgresql.Security.JWT.JwtUtil;
import com.project.springbootbasicwithpostgresql.Service.CustomUserDetailService;
import com.project.springbootbasicwithpostgresql.Service.CustomUserDetails;
import com.project.springbootbasicwithpostgresql.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final CustomUserDetailService customUserDetailService;


    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil, CustomUserDetailService customUserDetailService) {
        this.authenticationManager = authenticationManager;

        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.customUserDetailService = customUserDetailService;
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

        String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());
        Long accessTokenExpiresAt = jwtUtil.getTokenExpirationTime(accessToken);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        LogInResponse response = new LogInResponse(userDetails.getUsername(), roles,accessToken,refreshToken,accessTokenExpiresAt);

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

    @PostMapping("/refresh")
    public ResponseEntity<?>  getRefreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto){
        try{
            String refreshToken = refreshTokenRequestDto.getRefreshToken();
            String username = jwtUtil.extractUsername(refreshToken);

            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailService.loadUserByUsername(username);
            if (!jwtUtil.isRefreshTokenValid(refreshToken,userDetails)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Invalid refresh token"));
            }

            String newAccessToken = jwtUtil.generateAccessToken(username);
            Long accessTokenExpiresAt = jwtUtil.getTokenExpirationTime(newAccessToken);

            RefreshTokenResponseDto responseDto = new RefreshTokenResponseDto(newAccessToken,accessTokenExpiresAt,refreshToken);

            return ResponseEntity.ok(responseDto);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Invalid refresh token"));
        }

    }

}
