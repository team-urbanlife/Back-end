package com.wegotoo.application.schedule.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemoWriteServiceRequest {

    private String content;

    @Builder
    private MemoWriteServiceRequest(String content) {
        this.content = content;
    }

}
