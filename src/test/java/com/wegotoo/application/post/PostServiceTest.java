package com.wegotoo.application.post;

import static org.assertj.core.api.Assertions.assertThat;

import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.application.post.request.ContentEditServiceRequest;
import com.wegotoo.application.post.request.ContentWriteServiceRequest;
import com.wegotoo.application.post.request.PostEditServiceRequest;
import com.wegotoo.application.post.request.PostWriteServiceRequest;
import com.wegotoo.application.post.response.PostFindOneResponse;
import com.wegotoo.domain.post.Content;
import com.wegotoo.domain.post.ContentType;
import com.wegotoo.domain.post.Post;
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
        User user = getUser();
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

    @Test
    @DisplayName("유저가 게시글을 수정할 수 있다.")
    void editPost() throws Exception {
        // given
        User user = getUser();
        userRepository.save(user);

        Post post = getPost(user);
        postRepository.save(post);

        List<Content> contents = getContentList(post);
        contentRepository.saveAll(contents);

        ContentEditServiceRequest content1 = getContentEdit(contents.get(0).getId(), "첫 문단 수정");
        ContentEditServiceRequest content2 = getContentEdit(contents.get(1).getId(), "두 번째 문단 수정");

        List<ContentEditServiceRequest> contentRequests = List.of(content1, content2);

        PostEditServiceRequest request = PostEditServiceRequest.builder()
                .title("제목 수정")
                .contents(contentRequests)
                .build();

        // when
        postService.editPost(user.getId(), post.getId(), request);

        // then
        List<Post> response = postRepository.findAll();
        List<Content> contentList = contentRepository.findAll();
        assertThat(response.get(0))
                .extracting("id", "title")
                .contains(response.get(0).getId(), "제목 수정");

        assertThat(contentList.get(0))
                .extracting("id", "text")
                .contains(contentList.get(0).getId(), "첫 문단 수정");

    }

    private static List<Content> getContentList(Post post) {
        return IntStream.range(0, 2)
                .mapToObj(i -> Content.builder()
                        .type(ContentType.T)
                        .text("내용 " + i)
                        .post(post)
                        .build()).toList();
    }

    private static Post getPost(User user) {
        return Post.builder()
                .title("제목")
                .view(0)
                .user(user)
                .build();
    }

    private static User getUser() {
        return User.builder()
                .name("user")
                .email("user@email.com")
                .build();
    }

    private static ContentEditServiceRequest getContentEdit(Long id, String text) {
        return ContentEditServiceRequest.builder()
                .id(id)
                .type(ContentType.T)
                .text(text)
                .build();
    }
}