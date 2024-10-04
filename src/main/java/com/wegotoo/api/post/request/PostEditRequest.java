package com.wegotoo.api.post.request;

import com.wegotoo.application.post.request.PostEditServiceRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostEditRequest {

    private String title;

    private List<ContentEditRequest> contents = new ArrayList<>();

    @Builder
    private PostEditRequest(String title, List<ContentEditRequest> contents) {
        this.title = title;
        this.contents = contents;
    }

    public PostEditServiceRequest toService() {
        return PostEditServiceRequest.builder()
                .title(title)
                .contents(ContentEditRequest.toService(contents))
                .build();
    }
}
