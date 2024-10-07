package com.wegotoo.domain.post.repository.querydsl;

import static org.assertj.core.api.Assertions.assertThat;

import com.wegotoo.config.QueryDslConfig;
import com.wegotoo.domain.post.Content;
import com.wegotoo.domain.post.ContentType;
import com.wegotoo.domain.post.Post;
import com.wegotoo.domain.post.repository.ContentRepository;
import com.wegotoo.domain.post.repository.PostRepository;
import com.wegotoo.domain.post.repository.response.ContentTextQueryEntity;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QueryDslConfig.class)
class ContentRepositoryImplTest {

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
    @DisplayName("게시글 페이징 조회 테스트")
    void findAllPost() throws Exception {
        // given
        User user = User.builder()
                .name("user")
                .email("user@email.com")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("제목 ")
                .view(0)
                .user(user)
                .build();
        postRepository.save(post);

        Content content1 = getContent(post, ContentType.T, "글1");
        Content content2 = getContent(post, ContentType.IMAGE, "이미지1");
        Content content3 = getContent(post, ContentType.T, "글2");
        Content content4 = getContent(post, ContentType.IMAGE, "이미지2");

        contentRepository.saveAll(List.of(content1, content2, content3, content4));

        // when
        List<ContentTextQueryEntity> response = contentRepository.findAllPostWithContentText(List.of(1L));

        // then
        assertThat(response.size()).isEqualTo(2);
    }

    private static Content getContent(Post post, ContentType type, String text) {
        return Content.builder()
                .type(type)
                .post(post)
                .text(text)
                .build();
    }
}