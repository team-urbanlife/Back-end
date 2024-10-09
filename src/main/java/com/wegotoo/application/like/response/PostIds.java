package com.wegotoo.application.like.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostIds {

    private List<Long> postIds;

    @Builder
    private PostIds(List<Long> postIds) {
        this.postIds = postIds;
    }

    public static PostIds of(List<Long> postIds) {
        return PostIds.builder()
                .postIds(postIds)
                .build();
    }
}
