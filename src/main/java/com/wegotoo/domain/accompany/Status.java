package com.wegotoo.domain.accompany;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {

    RECRUIT("모집 중"),
    END("모집 완료");

    private final String text;
}
