package com.wegotoo.domain.schedule.repository.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class DetailedPlanQueryEntity {

    private String region;

    private Long sequence;

    private Double latitude;

    private Double longitude;

    private Long ScheduleDetailsId;

    @QueryProjection
    public DetailedPlanQueryEntity(String region, Long sequence, Double latitude, Double longitude,
                                   Long ScheduleDetailsId) {
        this.region = region;
        this.sequence = sequence;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ScheduleDetailsId = ScheduleDetailsId;
    }

}
