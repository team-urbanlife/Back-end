package com.wegotoo.application.post.response;

import com.wegotoo.domain.post.Content;
import com.wegotoo.domain.post.ContentType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContentResponse {

    private Long id;

    private ContentType type;

    private String text;

    @Builder
    private ContentResponse(Long id, ContentType type, String text) {
        this.id = id;
        this.type = type;
        this.text = text;
    }

    public static ContentResponse of(Content content) {
        return ContentResponse.builder()
                .id(content.getId())
                .type(content.getType())
                .text(content.getText())
                .build();
    }

    public static List<ContentResponse> toList(List<Content> contents) {
        return contents.stream().map(ContentResponse::of).toList();
    }
}
