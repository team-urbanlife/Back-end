package com.wegotoo.api.schedule.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemoEditServiceRequest {

    private String content;

    @Builder
    private MemoEditServiceRequest(String content) {
        this.content = content;
    }

}
