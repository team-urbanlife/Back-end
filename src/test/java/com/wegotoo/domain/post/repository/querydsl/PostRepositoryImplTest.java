package com.wegotoo.domain.post.repository.querydsl;

import static org.assertj.core.api.Assertions.assertThat;

import com.wegotoo.config.QueryDslConfig;
import com.wegotoo.domain.post.Post;
import com.wegotoo.domain.post.repository.PostRepository;
import com.wegotoo.domain.post.repository.response.PostQueryEntity;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QueryDslConfig.class)
class PostRepositoryImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("게시글 페이징 조회 테스트")
    void findAllPost() throws Exception {
        // given
        User user = User.builder()
                .name("user")
                .email("user@email.com")
                .build();
        userRepository.save(user);

        List<Post> posts = IntStream.range(0, 5)
                .mapToObj(i -> Post.builder()
                        .title("제목 " + i)
                        .view(0)
                        .user(user)
                        .registeredDateTime(LocalDateTime.now())
                        .build()).toList();
        postRepository.saveAll(posts);
        // when
        List<PostQueryEntity> response = postRepository.findAllPost(0, 4);

        // then
        assertThat(response.size()).isEqualTo(5);
    }
}