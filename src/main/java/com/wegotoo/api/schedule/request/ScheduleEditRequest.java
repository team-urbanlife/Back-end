package com.wegotoo.api.schedule.request;

import com.wegotoo.application.schedule.request.ScheduleEditServiceRequest;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleEditRequest {

    private String title;

    private String city;

    @NotNull(message = "여행 시작 일은 필수입니다.")
    private LocalDate startDate;

    @NotNull(message = "여행 종료 일은 필수입니다.")
    private LocalDate endDate;

    @Builder
    private ScheduleEditRequest(String title, String city, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ScheduleEditServiceRequest toService() {
        return ScheduleEditServiceRequest.builder()
                .title(title)
                .city(city)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
