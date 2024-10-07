package com.wegotoo.application.accompany;

import static com.wegotoo.domain.accompany.Gender.NO_MATTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.wegotoo.api.accompany.request.AccompanyEditServiceRequest;
import com.wegotoo.application.OffsetLimit;
import com.wegotoo.application.ServiceTestSupport;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AccompanyServiceTest extends ServiceTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccompanyRepository accompanyRepository;

    @Autowired
    AccompanyService accompanyService;

    final LocalDate START_DATE = LocalDate.of(2024, 9, 1);
    final LocalDate END_DATE = LocalDate.of(2024, 9, 3);
    final LocalDateTime DATE = LocalDateTime.of(2024, 9, 1, 12, 0);

    @AfterEach
    void tearDown() {
        accompanyRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("유저가 여행 모집글을 작성한다.")
    void createAccompany() throws Exception {
        // given
        User user = userRepository.save(createUser("user"));

        AccompanyCreateServiceRequest request = AccompanyCreateServiceRequest.builder()
                .startDate(START_DATE)
                .endDate(END_DATE)
                .title("제주도 여행 모집")
                .location("제주도")
                .latitude(0.0)
                .longitude(0.0)
                .personnel(3)
                .gender(NO_MATTER)
                .startAge(20)
                .endAge(29)
                .cost(1000000)
                .content("여행 관련 글")
                .build();

        // when
        accompanyService.createAccompany(user.getId(), request, DATE);

        // then
        List<Accompany> response = accompanyRepository.findAll();
        assertThat(response.get(0))
                .extracting("id", "title", "location", "latitude", "longitude", "personnel", "gender", "startAge",
                        "endAge", "cost")
                .contains(response.get(0).getId(), "제주도 여행 모집", "제주도", 0.0, 0.0, 3, NO_MATTER, 20, 29, 1000000);
    }

    @Test
    @DisplayName("유저가 여행 모집글을 수정한다.")
    void editAccompany() throws Exception {
        // given
        User user = userRepository.save(createUser("user"));

        Accompany accompany = Accompany.builder()
                .startDate(START_DATE)
                .endDate(END_DATE)
                .title("제주도 여행 모집")
                .location("제주도")
                .latitude(0.0)
                .longitude(0.0)
                .personnel(3)
                .gender(NO_MATTER)
                .startAge(20)
                .endAge(29)
                .cost(1000000)
                .content("여행 관련 글")
                .user(user)
                .build();
        accompanyRepository.save(accompany);

        AccompanyEditServiceRequest request = AccompanyEditServiceRequest.builder()
                .startDate(START_DATE)
                .endDate(END_DATE)
                .title("제주도 여행 모집 수정")
                .location("제주도 수정")
                .latitude(0.0)
                .longitude(0.0)
                .personnel(3)
                .gender(NO_MATTER)
                .startAge(20)
                .endAge(29)
                .cost(1000000)
                .content("여행 관련 글 수정")
                .build();
        // when
        accompanyService.editAccompany(user.getId(), accompany.getId(), request);

        // then
        List<Accompany> response = accompanyRepository.findAll();
        assertThat(response.get(0))
                .extracting("id", "title", "location", "latitude", "longitude", "personnel", "gender", "startAge",
                        "endAge", "cost", "content")
                .contains(response.get(0).getId(), "제주도 여행 모집 수정", "제주도 수정", 0.0, 0.0, 3, NO_MATTER, 20, 29, 1000000,
                        "여행 관련 글 수정");
    }

    @Test
    @DisplayName("유저가 여행 모집글을 삭제 한다")
    void deleteAccompany() throws Exception {
        // given
        User user = userRepository.save(createUser("user"));

        Accompany accompany = Accompany.builder()
                .startDate(START_DATE)
                .endDate(END_DATE)
                .title("제주도 여행 모집")
                .location("제주도")
                .latitude(0.0)
                .longitude(0.0)
                .personnel(3)
                .gender(NO_MATTER)
                .startAge(20)
                .endAge(29)
                .cost(1000000)
                .content("여행 관련 글")
                .user(user)
                .build();
        accompanyRepository.save(accompany);

        // when
        accompanyService.deleteAccompany(user.getId(), accompany.getId());
        // then
        List<Accompany> response = accompanyRepository.findAll();
        assertThat(response.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("유저가 여행 모집글을 조회할 수 있다.")
    void findAllAccompany() throws Exception {
        // given
        User user = userRepository.save(createUser("user"));

        List<Accompany> accompanies = IntStream.range(1, 5)
                .mapToObj(i -> Accompany.builder()
                        .startDate(START_DATE)
                        .endDate(END_DATE)
                        .title("제주도 여행 모집")
                        .location("제주도")
                        .latitude(0.0)
                        .longitude(0.0)
                        .personnel(3)
                        .gender(NO_MATTER)
                        .startAge(20)
                        .endAge(29)
                        .cost(1000000)
                        .content("여행 관련 글")
                        .user(user)
                        .registeredDateTime(LocalDateTime.of(2024, 9, i, 0, 0, 0))
                        .build()).toList();

        accompanyRepository.saveAll(accompanies);

        OffsetLimit offsetLimit = OffsetLimit.builder()
                .offset(0)
                .limit(4)
                .build();

        // when
        SliceResponse<AccompanyFindAllResponse> response = accompanyService.findAllAccompany(offsetLimit);

        // then
        assertThat(response.getContent().size()).isEqualTo(4);
    }

    @Test
    @DisplayName("유저가 작성한 여행 모집글을 조회할 수 있다.")
    void findAllUserAccompany() throws Exception {
        // given
        User user = userRepository.save(createUser("user"));

        List<Accompany> accompanies = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> Accompany.builder()
                        .startDate(START_DATE)
                        .endDate(END_DATE)
                        .title("제주도 여행 모집")
                        .location("제주도")
                        .latitude(0.0)
                        .longitude(0.0)
                        .personnel(3)
                        .gender(NO_MATTER)
                        .startAge(20)
                        .endAge(29)
                        .cost(1000000)
                        .content("여행 관련 글")
                        .user(user)
                        .registeredDateTime(LocalDateTime.of(2024, 9, i, 0, 0, 0))
                        .build()).toList();

        accompanyRepository.saveAll(accompanies);

        // when
        SliceResponse<AccompanyFindAllResponse> result = accompanyService.findAllUserAccompanies(
                user.getId(), OffsetLimit.of(1, 5));

        // then
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isLast()).isTrue();
        assertThat(result.isHasContent()).isTrue();
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getContent()).hasSize(5);
    }

    @Test
    @DisplayName("잘못된 유저 정보로 사용자 여행 모집글을 조회한다.")
    public void findAllUserAccompanyWithoutUser() {
        // given
        Long invalidUserId = 1L;

        // when // then
        assertThatThrownBy(() -> accompanyService.findAllUserAccompanies(invalidUserId, OffsetLimit.of(1, 5)))
                .isInstanceOf(BusinessException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("유저가 여행 모집글을 단건 조회할 수 있다.")
    void findOneAccompany() throws Exception {
        // given
        User user = userRepository.save(createUser("user"));

        List<Accompany> accompanies = IntStream.range(1, 5)
                .mapToObj(i -> Accompany.builder()
                        .startDate(START_DATE)
                        .endDate(END_DATE)
                        .title("제주도 여행 모집")
                        .location("제주도")
                        .latitude(0.0)
                        .longitude(0.0)
                        .personnel(3)
                        .gender(NO_MATTER)
                        .startAge(20)
                        .endAge(29)
                        .cost(1000000)
                        .content("여행 관련 글")
                        .user(user)
                        .registeredDateTime(LocalDateTime.of(2024, 9, i, 0, 0, 0))
                        .build()).toList();

        accompanyRepository.saveAll(accompanies);

        // when
        AccompanyFindOneResponse response = accompanyService.findOneAccompany(
                accompanies.get(0).getId());

        // then
        assertThat(response)
                .extracting("accompanyId", "startDate", "endDate", "title", "content", "userName", "views", "likeCount")
                .contains(response.getAccompanyId(), START_DATE, END_DATE, "제주도 여행 모집", "여행 관련 글", "user", 0L, 0L);
    }

    private User createUser(String name) {
        return User.builder()
                .name(name)
                .email(name + "@gmail.com")
                .build();
    }

}