package com.wegotoo.domain.city;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Type {

    DOMESTIC("국내"),
    OUTSIDE("국외");

    private final String type;

}
