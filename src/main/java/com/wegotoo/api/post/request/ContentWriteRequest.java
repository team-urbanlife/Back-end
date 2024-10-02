package com.wegotoo.api.post.request;

import com.wegotoo.application.post.request.ContentWriteServiceRequest;
import com.wegotoo.domain.post.ContentType;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContentWriteRequest {

    @NotNull(message = "타입은 필수입니다.")
    private ContentType type;

    @NotNull(message = "TEXT는 필수입니다.")
    private String text;

    @Builder
    private ContentWriteRequest(ContentType type, String text) {
        this.type = type;
        this.text = text;
    }

    public static ContentWriteServiceRequest of(ContentType type, String text) {
        return ContentWriteServiceRequest.builder()
                .type(type)
                .text(text)
                .build();
    }

    public static List<ContentWriteServiceRequest> toService(List<ContentWriteRequest> contents) {
        return contents.stream()
                .map(content -> ContentWriteRequest.of(content.type, content.text))
                .toList();
    }
}
