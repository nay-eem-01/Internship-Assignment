package com.project.springbootbasicwithpostgresql.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInResponse {

    private String username;
    private List<String> roles;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresAt;
}
