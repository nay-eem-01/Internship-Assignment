package com.project.springbootbasicwithpostgresql.Service;

import com.project.springbootbasicwithpostgresql.DTO.UserDto;
import com.project.springbootbasicwithpostgresql.ExceptionHandling.RoleNotFoundException;
import com.project.springbootbasicwithpostgresql.ExceptionHandling.UserAlreadyExistsException;
import com.project.springbootbasicwithpostgresql.Model.AppRole;
import com.project.springbootbasicwithpostgresql.Model.Users;
import com.project.springbootbasicwithpostgresql.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;


    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users users = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return CustomUserDetails.build(users);
    }

}
