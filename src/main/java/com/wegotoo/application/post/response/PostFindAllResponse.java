package com.wegotoo.application.post.response;

import com.wegotoo.domain.post.repository.response.ContentImageQueryEntity;
import com.wegotoo.domain.post.repository.response.ContentTextQueryEntity;
import com.wegotoo.domain.post.repository.response.PostQueryEntity;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    @Builder
    private PostFindAllResponse(Long postId, String title, String content, String thumbnail, String userName,
                                String userProfileImage, LocalDateTime registeredDateTime) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.userName = userName;
        this.userProfileImage = userProfileImage;
        this.registeredDateTime = registeredDateTime;
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
                .build();
    }

    public static List<PostFindAllResponse> toList(List<PostQueryEntity> posts,
                                                   List<ContentTextQueryEntity> texts,
                                                   List<ContentImageQueryEntity> images) {
        Map<Long, String> textMap = toTextMap(texts);
        Map<Long, String> imageMap = toImageMap(images);
        return posts.stream()
                .map(post -> PostFindAllResponse.of(post, textMap.get(post.getId()), imageMap.get(post.getId())))
                .toList();
    }

    private static Map<Long, String> toTextMap(List<ContentTextQueryEntity> contents) {
        if (contents == null) {
            return Collections.emptyMap();
        }
        return contents.stream()
                .collect(Collectors.toMap(
                        ContentTextQueryEntity::getPostId,
                        ContentTextQueryEntity::getText,
                        (existing, replacement) -> existing
                ));
    }

    private static Map<Long, String> toImageMap(List<ContentImageQueryEntity> contents) {
        if (contents == null) {
            return Collections.emptyMap();
        }
        return contents.stream()
                .collect(Collectors.toMap(
                        ContentImageQueryEntity::getPostId,
                        ContentImageQueryEntity::getImage,
                        (existing, replacement) -> existing
                ));
    }
}
