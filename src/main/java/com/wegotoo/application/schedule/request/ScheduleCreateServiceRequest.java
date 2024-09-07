package com.wegotoo.application.schedule.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ScheduleCreateServiceRequest {

    private String city;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder
    private ScheduleCreateServiceRequest(String city, LocalDate startDate, LocalDate endDate) {
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long totalTravelDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

}
