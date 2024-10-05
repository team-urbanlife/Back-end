package com.wegotoo.application.post.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditServiceRequest {

    private String title;

    private List<ContentEditServiceRequest> contents = new ArrayList<>();

    @Builder
    private PostEditServiceRequest(String title, List<ContentEditServiceRequest> contents) {
        this.title = title;
        this.contents = contents;
    }
}
