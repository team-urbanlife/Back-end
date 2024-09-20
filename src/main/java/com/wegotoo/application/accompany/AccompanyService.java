package com.wegotoo.application.accompany;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.api.accompany.request.AccompanyEditServiceRequest;
import com.wegotoo.application.accompany.request.AccompanyCreateServiceRequest;
import com.wegotoo.domain.accompany.Accompany;
import com.wegotoo.domain.accompany.repository.AccompanyRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AccompanyService {

    private final AccompanyRepository accompanyRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createAccompany(Long userId, AccompanyCreateServiceRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        accompanyRepository.save(request.toEntity(user));
    }

    @Transactional
    public void editAccompany(Long userId, Long accompanyId, AccompanyEditServiceRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Accompany accompany = accompanyRepository.findByIdAndUser(accompanyId, user)
                .orElseThrow(() -> new BusinessException(ACCOMPANY_NOT_FOUND));

        accompany.edit(request);
    }

}
