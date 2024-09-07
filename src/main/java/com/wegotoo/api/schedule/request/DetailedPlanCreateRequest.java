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

    @NotNull(message = "타입은 필수입니다.")
    private String type;

    private String name;

    private String memo;

    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;

    @Builder
    private DetailedPlanCreateRequest(LocalDate date, String type, String name, String memo, Double latitude,
                                      Double longitude) {
        this.date = date;
        this.type = type;
        this.name = name;
        this.memo = memo;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DetailedPlanCreateServiceRequest toService() {
        return DetailedPlanCreateServiceRequest.builder()
                .date(date)
                .type(Type.fromKey(type))
                .name(name)
                .memo(memo)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

}
