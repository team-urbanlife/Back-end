package com.wegotoo.application;

import static java.lang.Math.max;
import static java.lang.Math.min;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OffsetLimit {

    private static final Integer MAX_SIZE = 2000;
    private Integer offset;
    private Integer limit;

    @Builder
    private OffsetLimit(Integer offset, Integer limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public static OffsetLimit of(Integer page, Integer size) {
        return OffsetLimit.builder()
                .offset(calculateOffset(page, size))
                .limit(size)
                .build();
    }

    private static Integer calculateOffset(Integer page, Integer size) {
        return (max(1, page) - 1) * min(size, MAX_SIZE);
    }
}
