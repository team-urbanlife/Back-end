package com.wegotoo.application.schedule.request;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ScheduleEditServiceRequest {

    private String title;

    private String city;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder
    private ScheduleEditServiceRequest(String title, String city, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long totalTravelDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

}
