package com.wegotoo.infra.security.jwt.provider;

import com.wegotoo.domain.user.Role;
import com.wegotoo.infra.security.jwt.factory.JwtTokenFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final JwtTokenFactory tokenFactory;

    public JwtTokenProvider(
            JwtTokenFactory tokenFactory
    ) {
        this.tokenFactory = tokenFactory;
    }

    public String createAccessToken(Long id, String email, Role role) {
        return tokenFactory.createAccessToken(id, email, role.getKey());
    }

    public String createRefreshToken() {
        return tokenFactory.createRefreshToken();
    }

}
