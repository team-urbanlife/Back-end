package com.wegotoo.domain.post.repository.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostQueryEntity {

    private Long id;

    private String title;

    private String userName;

    private String userProfileImage;

    private LocalDateTime registrationDate;

    @Builder
    @QueryProjection
    public PostQueryEntity(Long id, String title, String userName, String userProfileImage,
                            LocalDateTime registrationDate) {
        this.id = id;
        this.title = title;
        this.userName = userName;
        this.userProfileImage = userProfileImage;
        this.registrationDate = registrationDate;
    }

}
