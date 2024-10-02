package com.wegotoo.api.post.request;

import com.wegotoo.application.post.request.PostWriteServiceRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostWriteRequest {

    @NotNull(message = "제목은 필수입니다.")
    private String title;

    @Valid
    private List<ContentWriteRequest> contents = new ArrayList<>();

    @Builder
    private PostWriteRequest(String title, List<ContentWriteRequest> contents) {
        this.title = title;
        this.contents = contents;
    }

    public PostWriteServiceRequest toService() {
        return PostWriteServiceRequest.builder()
                .title(title)
                .contents(ContentWriteRequest.toService(contents))
                .build();
    }
}
