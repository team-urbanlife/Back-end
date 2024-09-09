package com.wegotoo.application.schedule.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoWriteRequest {

    @NotNull(message = "내용은 필수입니다.")
    private String content;

    @Builder
    private MemoWriteRequest(String content) {
        this.content = content;
    }

    public MemoWriteServiceRequest toService() {
        return MemoWriteServiceRequest.builder()
                .content(content)
                .build();
    }

}
