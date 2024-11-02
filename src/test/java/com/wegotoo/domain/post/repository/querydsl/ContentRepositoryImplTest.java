package com.wegotoo.domain.post.repository.querydsl;

import static org.assertj.core.api.Assertions.assertThat;

import com.wegotoo.domain.DataJpaTestSupport;
import com.wegotoo.domain.post.Content;
import com.wegotoo.domain.post.ContentType;
import com.wegotoo.domain.post.Post;
import com.wegotoo.domain.post.repository.ContentRepository;
import com.wegotoo.domain.post.repository.PostRepository;
import com.wegotoo.domain.post.repository.response.ContentQueryEntity;
import com.wegotoo.domain.user.Role;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ContentRepositoryImplTest extends DataJpaTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ContentRepository contentRepository;

    @AfterEach
    void tearDown() {
        contentRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("게시글 ID로 내용 조회")
    public void findAllByPostIds() {
        // given
        User user = userRepository.save(createUser("userA"));
        Post post = postRepository.save(createPost(user));

        List<Content> contents = contentRepository.saveAll(List.of(
                createContent(post, ContentType.T, "글1"),
                createContent(post, ContentType.IMAGE, "이미지1"),
                createContent(post, ContentType.T, "글2"),
                createContent(post, ContentType.IMAGE, "이미지2")
        ));

        // when
        Map<Long, List<ContentQueryEntity>> result = contentRepository.findAllByPostIds(List.of(post.getId()));

        // then
        assertThat(result.get(post.getId())).hasSize(4);
    }


    private static User createUser(String nickname) {
        return User.builder()
                .name(nickname)
                .email(nickname + "@gmail.com")
                .role(Role.USER)
                .build();
    }

    private static Post createPost(User user) {
        return Post.builder()
                .title("게시글 제목")
                .user(user)
                .build();
    }

    private static Content createContent(Post post, ContentType type, String text) {
        return Content.builder()
                .type(type)
                .post(post)
                .text(text)
                .build();
    }

}