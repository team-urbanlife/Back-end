package com.wegotoo.application.post.response;

import com.wegotoo.domain.post.PostContents;
import com.wegotoo.domain.post.repository.response.PostQueryEntity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostFindAllResponse {

    private Long postId;

    private String title;

    private String content;

    private String thumbnail;

    private String userName;

    private String userProfileImage;

    private LocalDateTime registeredDateTime;

    private Long likeCount;

    @Builder
    private PostFindAllResponse(Long postId, String title, String content, String thumbnail, String userName,
                                String userProfileImage, LocalDateTime registeredDateTime, Long likeCount) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.userName = userName;
        this.userProfileImage = userProfileImage;
        this.registeredDateTime = registeredDateTime;
        this.likeCount = likeCount;
    }

    public static PostFindAllResponse of(PostQueryEntity post, String content, String thumbnail) {
        return PostFindAllResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(content)
                .thumbnail(thumbnail)
                .userName(post.getUserName())
                .userProfileImage(post.getUserProfileImage())
                .registeredDateTime(post.getRegistrationDate())
                .likeCount(post.getLikeCount())
                .build();
    }

    public static List<PostFindAllResponse> toList(List<PostQueryEntity> posts, PostContents postContents) {
        return posts.stream()
                .map(post -> PostFindAllResponse.of(post,
                        postContents.findFirstTextByPostId(post.getId()),
                        postContents.findFirstImageByPostId(post.getId()))
                )
                .toList();
    }

}
