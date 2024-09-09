package com.wegotoo.application.schedule.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MovePlanRequest {

    private boolean isMoveUp;

    @Builder
    private MovePlanRequest(boolean isMoveUp) {
        this.isMoveUp = isMoveUp;
    }

}
