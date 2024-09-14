package com.wegotoo.infra.security.user;

import com.wegotoo.infra.security.util.PasswordUtils;
import io.jsonwebtoken.Claims;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    @Builder
    private CustomUserDetails(Long id, String username, String password,
                              Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public static CustomUserDetails of(Claims claims) {
        return CustomUserDetails.builder()
                .id(convertToLongId(claims.getSubject()))
                .username(claims.get("email", String.class))
                .password(PasswordUtils.createRandomPassword())
                .authorities(convertRolesToAuthorities(claims.get("role", String.class)))
                .build();
    }

    private static Long convertToLongId(String id) {
        return Long.valueOf(id);
    }

    private static List<? extends GrantedAuthority> convertRolesToAuthorities(String... roles) {
        return Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }

}
