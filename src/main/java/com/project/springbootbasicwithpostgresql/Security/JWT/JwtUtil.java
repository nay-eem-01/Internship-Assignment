package com.project.springbootbasicwithpostgresql.Security.JWT;
import com.project.springbootbasicwithpostgresql.Service.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret:TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V}")
    private String secretKey;

    @Value("${jwt.expiration:120000}") // 1 hour default
    private Long jwtExpiration;

    @Value("${jwt.refresh.expiration:604800000}") // 7 days
    private Long refreshTokenExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }
    public String extractTokenType(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("type",String.class);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public String generateAccessToken(String username) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("type","access");
        System.out.println("This is first - access token");
        return generateToken(claims, username,jwtExpiration);
    }

    public String generateRefreshToken(String username) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("type","refresh");
        System.out.println("This is 2nd - refresh token");
        return generateToken(claims, username,refreshTokenExpiration);
    }

    public String generateToken(Map<String, Object> extractClaims, String username,Long expiration) {
        return Jwts.builder()
                .claims(extractClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isAccessTokenValid(String token, CustomUserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            final String tokenType = extractTokenType(token);
            return (username.equals(userDetails.getUsername())
                    && !isTokenExpired(token)
                    && "access".equals(tokenType));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshTokenValid(String token, CustomUserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            final String tokenType = extractTokenType(token);
            return (username.equals(userDetails.getUsername())
                    && !isTokenExpired(token)
                    && "refresh".equals(tokenType));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Long getTokenExpirationTime(String token) {
        return extractExpiration(token).getTime();
    }


}
