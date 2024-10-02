package com.wegotoo.application;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CursorResponse<ID, T extends IdType<ID>> {

    private List<T> content;

    private ID nextCursor;

    private boolean hasNext;

    private Integer size;

    @Builder
    private CursorResponse(List<T> content, ID nextCursor, boolean hasNext, Integer size) {
        this.content = content;
        this.nextCursor = nextCursor;
        this.hasNext = hasNext;
        this.size = size;
    }

    public static <ID, T extends IdType<ID>> CursorResponse<ID, T> of(List<T> content, Integer limit) {
        boolean hasNext = content.size() > limit;
        List<T> response = hasNext ? content.subList(0, limit) : content;
        ID nextCursor = hasNext ? response.get(response.size() - 1).getId() : null;

        return CursorResponse.<ID, T>builder()
                .content(response)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .size(response.size())
                .build();
    }

}
