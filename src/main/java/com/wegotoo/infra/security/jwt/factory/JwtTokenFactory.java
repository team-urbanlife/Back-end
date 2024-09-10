package com.wegotoo.infra.security.jwt.factory;

import com.wegotoo.infra.security.jwt.provider.SecretKeyProvider;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenFactory {

    private final Long accessTokenExpirationPeriod;
    private final Long refreshTokenExpirationPeriod;
    private final SecretKey secretKey;

    public JwtTokenFactory(
            SecretKeyProvider secretKeyProvider,
            @Value("${jwt.access.expiration}") Long accessTokenExpirationPeriod,
            @Value("${jwt.refresh.expiration}") Long refreshTokenExpirationPeriod
    ) {
        this.secretKey = secretKeyProvider.getSecretKey();
        this.accessTokenExpirationPeriod = accessTokenExpirationPeriod;
        this.refreshTokenExpirationPeriod = refreshTokenExpirationPeriod;
    }

    public String createAccessToken(Long id, String email, String role) {
        return Jwts.builder()
                .subject(String.valueOf(id))
                .claim("email", email)
                .claim("role", role)
                .expiration(generateCompletionTime(accessTokenExpirationPeriod))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken() {
        return Jwts.builder()
                .subject("refresh_token")
                .expiration(generateCompletionTime(refreshTokenExpirationPeriod))
                .signWith(secretKey)
                .compact();
    }

    private Date generateCompletionTime(Long expirationPeriod) {
        return new Date(System.currentTimeMillis() + expirationPeriod);
    }

}
