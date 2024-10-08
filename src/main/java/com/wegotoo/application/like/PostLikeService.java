package com.wegotoo.application.like;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.domain.like.PostLike;
import com.wegotoo.domain.like.repository.PostLikeRepository;
import com.wegotoo.domain.post.Post;
import com.wegotoo.domain.post.repository.PostRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostLikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public void likePost(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(POST_NOT_FOUND));

        if (postLikeRepository.findByUserIdAndPostId(user.getId(), post.getId()).isPresent()) {
            throw new BusinessException(LIKE_EXIST);
        };
        PostLike postLike = PostLike.create(user, post);

        postLikeRepository.save(postLike);
    }

    @Transactional
    public void unLikePost(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(POST_NOT_FOUND));

        PostLike postLike = postLikeRepository.findByUserIdAndPostId(user.getId(), post.getId())
                .orElseThrow(() -> new BusinessException(LIKE_NOT_FOUND));

        postLikeRepository.delete(postLike);
    }
}
