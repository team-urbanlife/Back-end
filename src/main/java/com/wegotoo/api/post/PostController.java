package com.wegotoo.api.post;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.api.post.request.PostWriteRequest;
import com.wegotoo.application.post.PostService;
import com.wegotoo.application.post.response.PostFindOneResponse;
import com.wegotoo.infra.resolver.auth.Auth;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/v1/posts")
    public ApiResponse<PostFindOneResponse> writePost(@Auth Long userId,
                                                      @RequestBody @Valid PostWriteRequest request) {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        return ApiResponse.ok(postService.writePost(userId, request.toService(), now));
    }
}
