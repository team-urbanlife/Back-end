package com.wegotoo.infra.security.jwt.provider;

import com.wegotoo.domain.user.Role;
import com.wegotoo.infra.security.jwt.factory.JwtTokenFactory;
import com.wegotoo.infra.security.jwt.validator.JwtTokenValidator;
import com.wegotoo.infra.security.user.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final JwtTokenFactory tokenFactory;
    private final JwtTokenValidator tokenValidator;

    public JwtTokenProvider(
            SecretKeyProvider secretKeyProvider,
            JwtTokenFactory tokenFactory,
            JwtTokenValidator tokenValidator
    ) {
        this.secretKey = secretKeyProvider.getSecretKey();
        this.tokenFactory = tokenFactory;
        this.tokenValidator = tokenValidator;
    }

    public String createAccessToken(Long id, String email, Role role) {
        return tokenFactory.createAccessToken(id, email, role.getKey());
    }

    public String createRefreshToken(Long id) {
        return tokenFactory.createRefreshToken(id);
    }

    public boolean isValid(String token) {
        return tokenValidator.isValid(token);
    }

    public Optional<Long> extractSubId(String token) {
        if (!tokenValidator.isValidToken(token)) {
            return Optional.empty();
        }

        CustomUserDetails userDetails = CustomUserDetails.of(extractClaims(token));
        return Optional.of(userDetails.getId());
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = CustomUserDetails.of(extractClaims(token));

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
