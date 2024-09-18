package com.wegotoo.api.schedule.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoEditRequest {

    @NotNull(message = "내용은 필수입니다.")
    private String content;

    @Builder
    private MemoEditRequest(String content) {
        this.content = content;
    }

    public MemoEditServiceRequest toService() {
        return MemoEditServiceRequest.builder()
                .content(content)
                .build();
    }
}
