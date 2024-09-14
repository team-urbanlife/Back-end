package com.wegotoo.domain.schedule.repository.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleQueryEntity {

    private Long Id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long participants;

    @QueryProjection
    public ScheduleQueryEntity(Long id, String title, LocalDate startDate, LocalDate endDate, Long participants) {
        Id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
    }
}
