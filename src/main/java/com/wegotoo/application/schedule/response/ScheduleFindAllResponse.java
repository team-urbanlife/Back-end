package com.wegotoo.application.schedule.response;

import com.wegotoo.domain.schedule.repository.response.ScheduleQueryEntity;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleFindAllResponse {

    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long participants;

    @Builder
    private ScheduleFindAllResponse(Long id, String title, LocalDate startDate, LocalDate endDate, Long participants) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
    }

    public static List<ScheduleFindAllResponse> toList(List<ScheduleQueryEntity> schedules) {
        return schedules.stream()
                .map(schedule -> ScheduleFindAllResponse.of(schedule))
                .toList();
    }

    private static ScheduleFindAllResponse of(ScheduleQueryEntity schedule) {
        return ScheduleFindAllResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .participants(schedule.getParticipants())
                .build();
    }
}
