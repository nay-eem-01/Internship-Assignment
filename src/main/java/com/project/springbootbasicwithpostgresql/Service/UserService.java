package com.project.springbootbasicwithpostgresql.Service;

import com.project.springbootbasicwithpostgresql.DTO.UserDto;

public interface UserService {
    UserDto saveUserToDB(UserDto userDto);
}
