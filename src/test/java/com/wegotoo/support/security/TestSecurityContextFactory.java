package com.wegotoo.support.security;

import com.wegotoo.infra.security.user.CustomUserDetails;
import com.wegotoo.infra.security.util.PasswordUtils;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class TestSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {

    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserDetails userDetails = CustomUserDetails.builder()
                .id(1L)
                .username("user@email.com")
                .password(PasswordUtils.createRandomPassword())
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        context.setAuthentication(token);

        return context;
    }

}
