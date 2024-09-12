package com.wegotoo.api.schedule.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailedPlanMoveRequest {

    @NotNull(message = "세부 계획의 ID 값은 필수입니다.")
    private Long detailedPlanId;

    @NotNull(message = "순서는 필수입니다.")
    private Long sequence;

    @Builder
    private DetailedPlanMoveRequest(Long detailedPlanId, Long sequence) {
        this.detailedPlanId = detailedPlanId;
        this.sequence = sequence;
    }
}
