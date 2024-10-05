package com.wegotoo.api.post.request;

import com.wegotoo.application.post.request.ContentEditServiceRequest;
import com.wegotoo.domain.post.ContentType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContentEditRequest {

    private Long id;

    private ContentType type;

    private String text;

    @Builder
    private ContentEditRequest(Long id, ContentType type, String text) {
        this.id = id;
        this.type = type;
        this.text = text;
    }

    public static ContentEditServiceRequest of(Long id, ContentType type, String text) {
        return ContentEditServiceRequest.builder()
                .id(id)
                .type(type)
                .text(text)
                .build();
    }

    public static List<ContentEditServiceRequest> toService(List<ContentEditRequest> contents) {
        return contents.stream()
                .map(content -> ContentEditRequest.of(content.id, content.type, content.getText()))
                .toList();
    }
}
