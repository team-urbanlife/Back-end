package com.wegotoo.api.like;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.application.like.PostLikeService;
import com.wegotoo.application.like.response.PostIds;
import com.wegotoo.infra.resolver.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/v1/likes/posts/{postId}")
    public ApiResponse<Void> likePost(@Auth Long userId,
                                      @PathVariable Long postId) {
        postLikeService.likePost(userId, postId);
        return ApiResponse.ok();
    }

    @DeleteMapping("/v1/likes/posts/{postId}")
    public ApiResponse<Void> nuLikePost(@Auth Long userId,
                                        @PathVariable Long postId) {
        postLikeService.unLikePost(userId, postId);
        return ApiResponse.ok();
    }

    @GetMapping("/v1/likes/posts")
    public ApiResponse<PostIds> findPostIds(@Auth Long userId) {
        return ApiResponse.ok(postLikeService.findPostIds(userId));
    }
}
