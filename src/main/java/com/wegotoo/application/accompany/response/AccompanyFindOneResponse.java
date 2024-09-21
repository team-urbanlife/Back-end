package com.wegotoo.application.accompany.response;

import com.wegotoo.domain.accompany.Gender;
import com.wegotoo.domain.accompany.repository.response.AccompanyFindOneQueryEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccompanyFindOneResponse {

    private Long accompanyId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String title;

    private String content;

    private String userName;

    private LocalDateTime registeredDateTime;

    private Long views;

    private Long likeCount;

    private String location;

    private int personnel;

    private Gender gender;

    private int startAge;

    private int endAge;

    private int cost;

    @Builder
    private AccompanyFindOneResponse(Long accompanyId, LocalDate startDate, LocalDate endDate, String title,
                                     String content, String userName, LocalDateTime registeredDateTime, Long views,
                                     Long likeCount, String location, int personnel, Gender gender, int startAge,
                                     int endAge, int cost) {
        this.accompanyId = accompanyId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.content = content;
        this.userName = userName;
        this.registeredDateTime = registeredDateTime;
        this.views = views;
        this.likeCount = likeCount;
        this.location = location;
        this.personnel = personnel;
        this.gender = gender;
        this.startAge = startAge;
        this.endAge = endAge;
        this.cost = cost;
    }

    public static AccompanyFindOneResponse of(AccompanyFindOneQueryEntity accompany) {
        return AccompanyFindOneResponse.builder()
                .accompanyId(accompany.getAccompanyId())
                .startDate(accompany.getStartDate())
                .endDate(accompany.getEndDate())
                .title(accompany.getTitle())
                .content(accompany.getContent())
                .userName(accompany.getUserName())
                .registeredDateTime(accompany.getRegisteredDateTime())
                .views(0L)
                .likeCount(0L)
                .location(accompany.getLocation())
                .personnel(accompany.getPersonnel())
                .gender(accompany.getGender())
                .startAge(accompany.getStartAge())
                .endAge(accompany.getEndAge())
                .cost(accompany.getCost())
                .build();
    }
}
