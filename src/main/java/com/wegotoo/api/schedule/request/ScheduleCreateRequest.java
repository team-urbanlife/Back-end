package com.wegotoo.api.schedule.request;

import com.wegotoo.application.schedule.request.ScheduleCreateServiceRequest;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleCreateRequest {

    @NotNull(message = "도시 선택은 필수입니다.")
    private String city;

    @NotNull(message = "여행 시작 일은 필수입니다.")
    private LocalDate startDate;

    @NotNull(message = "여행 종료 일은 필수입니다.")
    private LocalDate endDate;

    @Builder
    private ScheduleCreateRequest(String city, LocalDate startDate, LocalDate endDate) {
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ScheduleCreateServiceRequest toService() {
        return ScheduleCreateServiceRequest.builder()
                .city(city)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
