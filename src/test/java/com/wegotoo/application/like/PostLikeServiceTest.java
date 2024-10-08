package com.wegotoo.application.like;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.domain.like.PostLike;
import com.wegotoo.domain.like.repository.PostLikeRepository;
import com.wegotoo.domain.post.Post;
import com.wegotoo.domain.post.repository.PostRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PostLikeServiceTest extends ServiceTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    PostLikeService postLikeService;

    @AfterEach
    void tearDown() {
        postLikeRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("유저가 게시글에 좋아요를 누른다.")
    void likePost() throws Exception {
        // given
        User user = User.builder()
                .name("user")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("제목")
                .user(user)
                .view(0)
                .build();
        postRepository.save(post);
        // when
        postLikeService.likePost(user.getId(), post.getId());

        // then
        List<PostLike> response = postLikeRepository.findAll();
        assertThat(response.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("유저가 게시글 좋아요를 취소한다.")
    void unLikePost() throws Exception {
        // given
        User user = User.builder()
                .name("user")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("제목")
                .user(user)
                .view(0)
                .build();
        postRepository.save(post);

        PostLike postLike = PostLike.builder()
                .post(post)
                .user(user)
                .build();
        postLikeRepository.save(postLike);
        // when
        postLikeService.unLikePost(user.getId(), post.getId());

        // then
        List<PostLike> response = postLikeRepository.findAll();
        assertThat(response.size()).isEqualTo(0);
    }

}