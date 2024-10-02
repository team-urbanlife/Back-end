package com.wegotoo.application.post.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostWriteServiceRequest {

    private String title;

    private List<ContentWriteServiceRequest> contents = new ArrayList<>();

    @Builder
    private PostWriteServiceRequest(String title, List<ContentWriteServiceRequest> contents) {
        this.title = title;
        this.contents = contents;
    }

}
