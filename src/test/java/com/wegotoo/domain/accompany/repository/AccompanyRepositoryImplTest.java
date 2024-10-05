package com.wegotoo.domain.accompany.repository;

import static com.wegotoo.domain.accompany.Gender.NO_MATTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.wegotoo.config.QueryDslConfig;
import com.wegotoo.domain.accompany.Accompany;
import com.wegotoo.domain.accompany.repository.response.AccompanyFindAllQueryEntity;
import com.wegotoo.domain.accompany.repository.response.AccompanyFindOneQueryEntity;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QueryDslConfig.class)
class AccompanyRepositoryImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccompanyRepository accompanyRepository;

    private static final LocalDate START_DATE = LocalDate.of(2020, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2020, 12, 31);

    @AfterEach
    void tearDown() {
        accompanyRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("조회 테스트")
    public void findAllAccompany() {
        // given
        User user = userRepository.save(createUser("user"));
        Accompany accompany = accompanyRepository.save(createAccompany("제주도", user, LocalDateTime.now()));

        // when
        List<AccompanyFindAllQueryEntity> response = accompanyRepository.accompanyFindAll(0, 4);

        // then
        assertThat(response.get(0))
                .extracting("accompanyId", "startDate", "endDate", "title", "content", "userName")
                .contains(response.get(0).getAccompanyId(), START_DATE, END_DATE, "제주도 여행 모집", "여행 관련 글", "user");
    }

    @Test
    @DisplayName("사용자 동행 조회 테스트")
    public void findAllAccompanyByUserId() {
        // given
        LocalDateTime registeredDateTimeFirst = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime registeredDateTimeSecond = LocalDateTime.of(2020, 1, 2, 0, 0);

        User userA = userRepository.save(createUser("userA"));

        List<Accompany> accompanies = accompanyRepository.saveAll(
                List.of(createAccompany("제주도", userA, registeredDateTimeFirst),
                        createAccompany("강원도", userA, registeredDateTimeSecond)));

        // when
        List<AccompanyFindAllQueryEntity> result = accompanyRepository.findAllAccompanyByUserId(
                userA.getId(), 0, 4);

        // then
        assertThat(result).hasSize(2)
                .extracting("accompanyId", "startDate", "endDate", "title", "content", "userName", "registeredDateTime")
                .containsExactly(
                        tuple(accompanies.get(1).getId(), START_DATE, END_DATE, "강원도 여행 모집", "여행 관련 글", "userA",
                                registeredDateTimeSecond),
                        tuple(accompanies.get(0).getId(), START_DATE, END_DATE, "제주도 여행 모집", "여행 관련 글", "userA",
                                registeredDateTimeFirst)
                );
    }

    @Test
    @DisplayName("단건 조회 테스트")
    public void findOneAccompany() {
        // given
        User user = userRepository.save(createUser("user"));
        Accompany accompany = accompanyRepository.save(createAccompany("제주도", user, LocalDateTime.now()));

        // when
        AccompanyFindOneQueryEntity response = accompanyRepository.accompanyFindOne(accompany.getId());

        // then
        assertThat(response)
                .extracting("accompanyId", "startDate", "endDate", "title", "content", "userName")
                .contains(response.getAccompanyId(), START_DATE, END_DATE, "제주도 여행 모집", "여행 관련 글", "user");
    }

    private User createUser(String name) {
        return User.builder()
                .name(name)
                .email(name + "@gmail.com")
                .build();
    }

    private Accompany createAccompany(String location, User user, LocalDateTime registeredDateTime) {
        return Accompany.builder()
                .startDate(START_DATE)
                .endDate(END_DATE)
                .title(location + " 여행 모집")
                .location(location)
                .latitude(0.0)
                .longitude(0.0)
                .personnel(3)
                .gender(NO_MATTER)
                .startAge(20)
                .endAge(29)
                .cost(1000000)
                .content("여행 관련 글")
                .registeredDateTime(registeredDateTime)
                .user(user)
                .build();
    }

}