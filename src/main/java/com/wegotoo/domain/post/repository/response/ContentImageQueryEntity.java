package com.wegotoo.domain.post.repository.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContentImageQueryEntity {

    private Long postId;

    private Long contentId;

    private String image;

    @Builder
    @QueryProjection
    public ContentImageQueryEntity(Long postId, Long contentId, String image) {
        this.postId = postId;
        this.contentId = contentId;
        this.image = image;
    }
}
