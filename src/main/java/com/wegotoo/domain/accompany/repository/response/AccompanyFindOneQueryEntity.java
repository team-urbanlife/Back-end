package com.wegotoo.domain.accompany.repository.response;

import com.querydsl.core.annotations.QueryProjection;
import com.wegotoo.domain.accompany.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccompanyFindOneQueryEntity {

    private Long accompanyId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String title;

    private String content;

    private String userName;

    private LocalDateTime registeredDateTime;

    private String location;

    private int personnel;

    private Gender gender;

    private int startAge;

    private int endAge;

    private int cost;

    private String userProfileImage;

    @QueryProjection
    public AccompanyFindOneQueryEntity(Long accompanyId, LocalDate startDate, LocalDate endDate, String title,
                                       String content, String userName, LocalDateTime registeredDateTime,
                                       String location, int personnel, Gender gender, int startAge, int endAge,
                                       int cost, String userProfileImage) {
        this.accompanyId = accompanyId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.content = content;
        this.userName = userName;
        this.registeredDateTime = registeredDateTime;
        this.location = location;
        this.personnel = personnel;
        this.gender = gender;
        this.startAge = startAge;
        this.endAge = endAge;
        this.cost = cost;
        this.userProfileImage = userProfileImage;
    }

}
