package com.wegotoo.domain.accompany.repository.response;

import com.querydsl.core.annotations.QueryProjection;
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

    @QueryProjection
    public AccompanyFindOneQueryEntity(Long accompanyId, LocalDate startDate, LocalDate endDate, String title,
                                       String content, String userName, LocalDateTime registeredDateTime) {
        this.accompanyId = accompanyId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.content = content;
        this.userName = userName;
        this.registeredDateTime = registeredDateTime;
    }

}
