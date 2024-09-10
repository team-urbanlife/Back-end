package com.wegotoo.infra.security.user;

import com.wegotoo.domain.user.Role;
import com.wegotoo.domain.user.User;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private Long id;
    private String email;
    private Role role;

    @Builder
    public CustomOAuth2User(Long id, String email, Role role, Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey) {
        super(authorities, attributes, nameAttributeKey);
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public static CustomOAuth2User of(String nameAttributeKey, User user, Map<String, Object> attributes) {
        return CustomOAuth2User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .nameAttributeKey(nameAttributeKey)
                .authorities(Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())))
                .attributes(attributes)
                .build();
    }

}
