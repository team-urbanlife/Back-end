package com.wegotoo.application.post;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.application.post.request.PostWriteServiceRequest;
import com.wegotoo.application.post.response.ContentResponse;
import com.wegotoo.application.post.response.PostFindOneResponse;
import com.wegotoo.domain.post.Content;
import com.wegotoo.domain.post.Post;
import com.wegotoo.domain.post.repository.ContentRepository;
import com.wegotoo.domain.post.repository.PostRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostFindOneResponse writePost(Long userId, PostWriteServiceRequest request, LocalDateTime date) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Post post = Post.create(request.getTitle(), user, date);
        Post postSave = postRepository.save(post);

        List<Content> contents = request.getContents().stream().map(c -> Content.create(c.getType(), c.getText(), postSave))
                .toList();
        List<Content> contentsSave = contentRepository.saveAll(contents);

        return PostFindOneResponse.of(postSave, user, ContentResponse.toList(contentsSave));
    }

}

