package com.wegotoo.application.schedule.request;

import com.wegotoo.domain.schedule.Type;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DetailedPlanCreateServiceRequest {

    private LocalDate date;
    private Type type;
    private String name;
    private String memo;
    private Double latitude;
    private Double longitude;

    @Builder
    private DetailedPlanCreateServiceRequest(LocalDate date, Type type, String name, String memo, Double latitude,
                                             Double longitude) {
        this.date = date;
        this.type = type;
        this.name = name;
        this.memo = memo;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
