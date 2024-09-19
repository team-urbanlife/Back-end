package com.wegotoo.application.accompany.request;

import com.wegotoo.domain.accompany.Accompany;
import com.wegotoo.domain.accompany.Gender;
import com.wegotoo.domain.accompany.Status;
import com.wegotoo.domain.user.User;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccompanyCreateServiceRequest {

    private LocalDate startDate;

    private LocalDate endDate;

    private String title;

    private String location;

    private Double latitude;

    private Double longitude;

    private int personnel;

    private Gender gender;

    private int startAge;

    private int endAge;

    private int cost;

    private String content;

    @Builder
    private AccompanyCreateServiceRequest(LocalDate startDate, LocalDate endDate, String title, String location,
                                          Double latitude, Double longitude, int personnel,
                                          Gender gender,
                                          int startAge, int endAge, int cost, String content) {
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

    public Accompany toEntity(User user) {
        return Accompany.builder()
                .startDate(startDate)
                .endDate(endDate)
                .title(title)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .personnel(personnel)
                .gender(gender)
                .startAge(startAge)
                .endAge(endAge)
                .cost(cost)
                .content(content)
                .status(Status.RECRUIT)
                .user(user)
                .build();
    }

}
