package com.wegotoo.application.accompany.response;

import com.wegotoo.domain.accompany.repository.response.AccompanyFindAllQueryEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccompanyFindAllResponse {

    private Long accompanyId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String title;

    private String content;

    private String userName;

    private LocalDateTime registeredDateTime;

    private String userProfileImage;

    @Builder
    private AccompanyFindAllResponse(Long accompanyId, LocalDate startDate, LocalDate endDate, String title,
                                     String content,
                                     String userName, LocalDateTime registeredDateTime, String userProfileImage) {
        this.accompanyId = accompanyId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.content = content;
        this.userName = userName;
        this.registeredDateTime = registeredDateTime;
        this.userProfileImage = userProfileImage;
    }

    public static List<AccompanyFindAllResponse> toList(List<AccompanyFindAllQueryEntity> accompanys) {
        return accompanys.stream()
                .map(AccompanyFindAllResponse::of)
                .toList();
    }

    private static AccompanyFindAllResponse of(AccompanyFindAllQueryEntity accompany) {
        return AccompanyFindAllResponse.builder()
                .accompanyId(accompany.getAccompanyId())
                .startDate(accompany.getStartDate())
                .endDate(accompany.getEndDate())
                .title(accompany.getTitle())
                .content(accompany.getContent())
                .userName(accompany.getUserName())
                .registeredDateTime(accompany.getRegisteredDateTime())
                .userProfileImage(accompany.getUserProfileImage())
                .build();
    }

}
