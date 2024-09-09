package com.wegotoo.application.schedule.request;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DetailedPlanEditServiceRequest {

    private String name;
    private Double latitude;
    private Double longitude;

    @Builder
    private DetailedPlanEditServiceRequest(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
