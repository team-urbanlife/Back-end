package com.wegotoo.domain.notification.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {

    CHAT("채팅"),
    ;

    private final String description;

}
