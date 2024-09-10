package com.wegotoo.application.schedule.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailedPlanEditRequest {

    @NotNull(message = "지역은 필수입니다.")
    private String name;

    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;

    @Builder
    private DetailedPlanEditRequest(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public DetailedPlanEditServiceRequest toService() {
        return DetailedPlanEditServiceRequest.builder()
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

}
