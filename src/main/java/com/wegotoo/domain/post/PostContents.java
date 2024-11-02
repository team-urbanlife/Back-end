package com.wegotoo.domain.post;

import com.wegotoo.domain.post.repository.response.ContentQueryEntity;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostContents {

    private Map<Long, List<ContentQueryEntity>> postContents;

    @Builder
    private PostContents(Map<Long, List<ContentQueryEntity>> postContents) {
        this.postContents = postContents;
    }

    public static PostContents of(Map<Long, List<ContentQueryEntity>> postContents) {
        return PostContents.builder()
                .postContents(postContents)
                .build();
    }

    public String findFirstTextByPostId(Long postId) {
        return postContents.get(postId).stream()
                .filter(content -> content.isSameType(ContentType.T))
                .min(Comparator.comparing(ContentQueryEntity::getContentId))
                .map(ContentQueryEntity::getText)
                .orElse(null);
    }

    public String findFirstImageByPostId(Long postId) {
        return postContents.get(postId).stream()
                .filter(content -> content.isSameType(ContentType.IMAGE))
                .min(Comparator.comparing(ContentQueryEntity::getContentId))
                .map(ContentQueryEntity::getText)
                .orElse(null);
    }

}
