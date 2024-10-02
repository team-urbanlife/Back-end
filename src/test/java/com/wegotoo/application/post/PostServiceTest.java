package com.wegotoo.application.post;

import static org.assertj.core.api.Assertions.*;

import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.application.post.request.ContentWriteServiceRequest;
import com.wegotoo.application.post.request.PostWriteServiceRequest;
import com.wegotoo.application.post.response.PostFindOneResponse;
import com.wegotoo.domain.post.ContentType;
import com.wegotoo.domain.post.repository.ContentRepository;
import com.wegotoo.domain.post.repository.PostRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PostServiceTest extends ServiceTestSupport {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        contentRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("유저가 게시글을 작성할 수 있다.")
    void writePost() throws Exception {
        // given
        User user = User.builder()
                .name("user")
                .email("user@email.com")
                .build();
        userRepository.save(user);

        List<ContentWriteServiceRequest> contents = IntStream.range(0, 4)
                .mapToObj(i -> ContentWriteServiceRequest.builder()
                        .type(ContentType.T)
                        .text("내용 " + i)
                        .build()).toList();
        PostWriteServiceRequest request = PostWriteServiceRequest.builder()
                .title("제목")
                .contents(contents)
                .build();
        // when
        PostFindOneResponse response = postService.writePost(user.getId(), request, LocalDateTime.now());

        // then
        assertThat(response)
                .extracting("id", "title")
                .contains(response.getId(), "제목");
        assertThat(response.getContents().get(0))
                .extracting("id", "type", "text")
                .contains(1L, ContentType.T, "내용 0");
    }
}