package com.wegotoo.domain.post.repository.response;

import com.querydsl.core.annotations.QueryProjection;
import com.wegotoo.domain.post.ContentType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContentQueryEntity {

    private Long postId;
    private Long contentId;
    private String text;
    private ContentType contentType;

    @Builder
    @QueryProjection
    public ContentQueryEntity(Long postId, Long contentId, String text, ContentType contentType) {
        this.postId = postId;
        this.contentId = contentId;
        this.text = text;
        this.contentType = contentType;
    }

    public boolean isSameType(ContentType contentType) {
        return this.contentType.equals(contentType);
    }

}
