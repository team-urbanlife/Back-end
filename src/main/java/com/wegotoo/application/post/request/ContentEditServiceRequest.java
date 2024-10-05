package com.wegotoo.application.post.request;

import com.wegotoo.domain.post.ContentType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ContentEditServiceRequest {

    private Long id;

    private ContentType type;

    private String text;

    @Builder
    private ContentEditServiceRequest(Long id, ContentType type, String text) {
        this.id = id;
        this.type = type;
        this.text = text;
    }
}
