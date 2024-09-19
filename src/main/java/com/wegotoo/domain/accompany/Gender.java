package com.wegotoo.domain.accompany;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {

    MAN("남성"),
    WOMAN("여성"),
    NO_MATTER("상관없음");

    private final String label;

    public static Gender fromString(String value) {
        if (value == null) {
            return NO_MATTER;
        }

        switch (value.toUpperCase()) {
            case "MAN" -> {
                return MAN;
            }
            case "WOMAN" -> {
                return WOMAN;
            }
            default -> {
                return NO_MATTER;
            }
        }
    }

}
