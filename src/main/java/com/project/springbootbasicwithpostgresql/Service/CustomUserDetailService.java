package com.project.springbootbasicwithpostgresql.Service;

import com.project.springbootbasicwithpostgresql.Model.Users;
import com.project.springbootbasicwithpostgresql.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
                .orElseThrow(()-> new UsernameNotFoundException("User not found with username: " + username));

        return CustomUserDetails.build(users);
    }

    public Users SaveUserToDB(Users user){
        return userRepository.save(user);
    }

}
