package com.wegotoo.application.accompany;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.api.accompany.request.AccompanyEditServiceRequest;
import com.wegotoo.application.OffsetLimit;
import com.wegotoo.application.SliceResponse;
import com.wegotoo.application.accompany.request.AccompanyCreateServiceRequest;
import com.wegotoo.application.accompany.response.AccompanyFindAllResponse;
import com.wegotoo.application.accompany.response.AccompanyFindOneResponse;
import com.wegotoo.domain.accompany.Accompany;
import com.wegotoo.domain.accompany.repository.AccompanyRepository;
import com.wegotoo.domain.accompany.repository.response.AccompanyFindAllQueryEntity;
import com.wegotoo.domain.accompany.repository.response.AccompanyFindOneQueryEntity;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.List;
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
    public AccompanyFindOneResponse createAccompany(Long userId, AccompanyCreateServiceRequest request,
                                                    LocalDateTime date) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Accompany accompany = accompanyRepository.save(request.toEntity(user, date));

        AccompanyFindOneQueryEntity response = accompanyRepository.accompanyFindOne(accompany.getId());

        return AccompanyFindOneResponse.of(response);
    }

    public SliceResponse<AccompanyFindAllResponse> findAllAccompany(OffsetLimit offsetLimit) {
        List<AccompanyFindAllQueryEntity> accompany = accompanyRepository.accompanyFindAll(
                offsetLimit.getOffset(), offsetLimit.getLimit());

        return SliceResponse.of(AccompanyFindAllResponse.toList(accompany), offsetLimit.getOffset(),
                offsetLimit.getLimit());
    }

    public AccompanyFindOneResponse findOneAccompany(Long accompanyId) {
        // TODO 좋아요 기능 구현 시 조회수도 같이 구현 예정
        AccompanyFindOneQueryEntity accompany = accompanyRepository.accompanyFindOne(accompanyId);
        return AccompanyFindOneResponse.of(accompany);
    }

    @Transactional
    public void editAccompany(Long userId, Long accompanyId, AccompanyEditServiceRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Accompany accompany = accompanyRepository.findByIdAndUser(accompanyId, user)
                .orElseThrow(() -> new BusinessException(ACCOMPANY_NOT_FOUND));

        accompany.edit(request);
    }

    @Transactional
    public void deleteAccompany(Long userId, Long accompanyId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Accompany accompany = accompanyRepository.findByIdAndUser(accompanyId, user)
                .orElseThrow(() -> new BusinessException(ACCOMPANY_NOT_FOUND));

        accompanyRepository.delete(accompany);
    }

}
