package com.wegotoo.domain.post.repository.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContentTextQueryEntity {

    private Long postId;

    private Long contentId;

    private String text;

    @Builder
    @QueryProjection
    public ContentTextQueryEntity(Long postId, Long contentId, String text) {
        this.postId = postId;
        this.contentId = contentId;
        this.text = text;
    }
}
