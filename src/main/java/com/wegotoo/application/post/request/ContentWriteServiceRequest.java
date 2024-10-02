package com.wegotoo.application.post.request;

import com.wegotoo.domain.post.ContentType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ContentWriteServiceRequest {

    private ContentType type;

    private String text;

    @Builder
    private ContentWriteServiceRequest(ContentType type, String text) {
        this.type = type;
        this.text = text;
    }
}
