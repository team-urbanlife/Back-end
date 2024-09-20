package com.wegotoo.domain.accompany.repository;

import static com.wegotoo.domain.accompany.Gender.NO_MATTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wegotoo.config.QueryDslConfig;
import com.wegotoo.domain.accompany.Accompany;
import com.wegotoo.domain.accompany.repository.response.AccompanyFindAllQueryEntity;
import com.wegotoo.domain.accompany.repository.response.AccompanyFindOneQueryEntity;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.time.LocalDate;
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

    private final LocalDate START_DATE = LocalDate.of(2020, 1, 1);
    private final LocalDate END_DATE = LocalDate.of(2020, 12, 31);

    @AfterEach
    void tearDown() {
        accompanyRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("조회 테스트")
    void findAllAccompany() throws Exception {
        // given
        User user = User.builder()
                .email("user@gmail.com")
                .name("user")
                .build();
        userRepository.save(user);

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
        List<AccompanyFindAllQueryEntity> response = accompanyRepository.accompanyFindAll(0, 4);

        // then
        assertThat(response.get(0))
                .extracting("accompanyId", "startDate", "endDate", "title", "content", "userName")
                .contains(response.get(0).getAccompanyId(), START_DATE, END_DATE, "제주도 여행 모집", "여행 관련 글", "user");
    }

    @Test
    @DisplayName("단건 조회 테스트")
    void findOneAccompany() throws Exception {
        // given
        User user = User.builder()
                .email("user@gmail.com")
                .name("user")
                .build();
        userRepository.save(user);

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
        AccompanyFindOneQueryEntity response = accompanyRepository.accompanyFindOne(
                accompany.getId());

        // then
        assertThat(response)
                .extracting("accompanyId", "startDate", "endDate", "title", "content", "userName")
                .contains(response.getAccompanyId(), START_DATE, END_DATE, "제주도 여행 모집", "여행 관련 글", "user");
    }
}