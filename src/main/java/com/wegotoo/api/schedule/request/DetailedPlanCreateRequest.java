package com.wegotoo.api.schedule.request;

import com.wegotoo.application.schedule.request.DetailedPlanCreateServiceRequest;
import com.wegotoo.domain.schedule.Type;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailedPlanCreateRequest {

    @NotNull(message = "여행 일자는 필수입니다.")
    private LocalDate date;

    @NotNull(message = "장소는 필수입니다.")
    private String name;

    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;

    @Builder
    private DetailedPlanCreateRequest(LocalDate date, String name, Double latitude,
                                      Double longitude) {
        this.date = date;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DetailedPlanCreateServiceRequest toService() {
        return DetailedPlanCreateServiceRequest.builder()
                .date(date)
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

}
