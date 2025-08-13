package com.project.springbootbasicwithpostgresql.Security;

import com.project.springbootbasicwithpostgresql.Model.AppRole;
import com.project.springbootbasicwithpostgresql.Model.Users;
import com.project.springbootbasicwithpostgresql.Repository.UserRepository;
import com.project.springbootbasicwithpostgresql.Service.CustomUserDetailService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;

    public SecurityConfig(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/public").permitAll()
                        .requestMatchers("/user").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());;


        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUserName("intern")) {
                userRepository.save(new Users(
                        "intern",
                        passwordEncoder.encode("password123"),
                        AppRole.ROLE_USER
                ));
            }

            if (!userRepository.existsByUserName("admin")) {
                userRepository.save(new Users(
                        "admin",
                        passwordEncoder.encode("admin123"),
                        AppRole.ROLE_ADMIN
                ));
            }
        };
    }

}
