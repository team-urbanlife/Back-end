package com.wegotoo.application.schedule.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleResponse {

    private Long scheduleId;

    private List<TravelPlanResponse> travelPlanList;

    @Builder
    private ScheduleResponse(Long scheduleId, List<TravelPlanResponse> travelPlanList) {
        this.scheduleId = scheduleId;
        this.travelPlanList = travelPlanList;
    }

    public static ScheduleResponse of(Long scheduleId, List<TravelPlanResponse> travelPlanList) {
        return ScheduleResponse.builder()
                .scheduleId(scheduleId)
                .travelPlanList(travelPlanList)
                .build();
    }

}
