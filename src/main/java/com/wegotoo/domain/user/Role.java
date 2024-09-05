package com.wegotoo.domain.user;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;

}
