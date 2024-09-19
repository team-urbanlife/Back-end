package com.wegotoo.api.accompany.request;

import com.wegotoo.application.accompany.request.AccompanyCreateServiceRequest;
import com.wegotoo.domain.accompany.Gender;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccompanyCreateRequest {

    @NotNull(message = "여행 시작일은 필수입니다.")
    private LocalDate startDate;

    @NotNull(message = "여행 종료일은 필수입니다.")
    private LocalDate endDate;

    @NotNull(message = "제목은 필수입니다.")
    private String title;

    @NotNull(message = "도시는 필수입니다.")
    private String location;

    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;

    private int personnel;

    private String gender;

    private int startAge;

    private int endAge;

    private int cost;

    private String content;

    @Builder
    private AccompanyCreateRequest(LocalDate startDate, LocalDate endDate, String title, String location,
                                  Double latitude,
                                  Double longitude, int personnel, String gender, int startAge, int endAge, int cost,
                                  String content) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.personnel = personnel;
        this.gender = gender;
        this.startAge = startAge;
        this.endAge = endAge;
        this.cost = cost;
        this.content = content;
    }

    public AccompanyCreateServiceRequest toService() {
        return AccompanyCreateServiceRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .title(title)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .personnel(personnel)
                .gender(Gender.fromString(gender))
                .startAge(startAge)
                .endAge(endAge)
                .cost(cost)
                .content(content)
                .build();
    }

}

