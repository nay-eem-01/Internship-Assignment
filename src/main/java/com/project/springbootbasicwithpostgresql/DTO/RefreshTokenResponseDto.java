package com.project.springbootbasicwithpostgresql.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponseDto {
    private String accessToken;
    private Long accessTokenExpiresAt;
    private String refreshToken;
}
