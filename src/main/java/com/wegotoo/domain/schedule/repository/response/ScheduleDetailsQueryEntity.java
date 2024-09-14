package com.wegotoo.domain.schedule.repository.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ScheduleDetailsQueryEntity {

    private Long id;

    private LocalDate travelDay;

    @QueryProjection
    public ScheduleDetailsQueryEntity(Long id, LocalDate travelDay) {
        this.id = id;
        this.travelDay = travelDay;
    }

}
