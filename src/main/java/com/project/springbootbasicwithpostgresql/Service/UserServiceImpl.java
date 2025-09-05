package com.project.springbootbasicwithpostgresql.Service;

import com.project.springbootbasicwithpostgresql.DTO.UserDto;
import com.project.springbootbasicwithpostgresql.ExceptionHandling.RoleNotFoundException;
import com.project.springbootbasicwithpostgresql.ExceptionHandling.UserAlreadyExistsException;
import com.project.springbootbasicwithpostgresql.Model.AppRole;
import com.project.springbootbasicwithpostgresql.Model.Users;
import com.project.springbootbasicwithpostgresql.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto saveUserToDB(UserDto userDto) {


        Users newUser = modelMapper.map(userDto,Users.class);


        if (userRepository.existsByUserName(newUser.getUserName())) {
            throw new UserAlreadyExistsException("User with username '" + newUser.getUserName() + "' already exists");
        }

        if (!(newUser.getRole().equals(AppRole.ROLE_USER) || newUser.getRole().equals(AppRole.ROLE_ADMIN))){
            throw new RoleNotFoundException("Only USER and ADMIN are valid roles");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return modelMapper.map(userRepository.save(newUser),UserDto.class);
    }
}
