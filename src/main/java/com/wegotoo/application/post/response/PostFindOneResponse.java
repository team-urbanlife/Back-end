package com.wegotoo.application.post.response;

import com.wegotoo.domain.post.Post;
import com.wegotoo.domain.user.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostFindOneResponse {

    private Long id;

    private String title;

    private String userName;

    private String userProfileImage;

    private List<ContentResponse> contents = new ArrayList<>();

    private int views;

    private LocalDateTime registeredDateTime;

    private Long likeCount;

    @Builder
    private PostFindOneResponse(Long id, String title, String userName, String userProfileImage,
                                List<ContentResponse> contents,
                                int views, LocalDateTime registeredDateTime, Long likeCount) {
        this.id = id;
        this.title = title;
        this.userName = userName;
        this.userProfileImage = userProfileImage;
        this.contents = contents;
        this.views = views;
        this.registeredDateTime = registeredDateTime;
        this.likeCount = likeCount;
    }

    public static PostFindOneResponse of(Post post, User user, List<ContentResponse> contents, Long likeCount) {
        return PostFindOneResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .userName(user.getName())
                .userProfileImage(user.getProfileImage())
                .contents(contents)
                .views(post.getView())
                .registeredDateTime(post.getRegisteredDateTime())
                .likeCount(likeCount)
                .build();
    }
}
