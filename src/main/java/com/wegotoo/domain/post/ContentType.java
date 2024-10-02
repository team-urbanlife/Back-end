package com.wegotoo.domain.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {

    T("내용"),
    IMAGE("이미지");

    private final String label;
}
