package com.wegotoo.application.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wegotoo.api.schedule.request.MemoEditServiceRequest;
import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.application.schedule.request.MemoWriteServiceRequest;
import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.Memo;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.ScheduleGroup;
import com.wegotoo.domain.schedule.repository.DetailPlanRepository;
import com.wegotoo.domain.schedule.repository.MemoRepository;
import com.wegotoo.domain.schedule.repository.ScheduleDetailsRepository;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemoServiceTest extends ServiceTestSupport {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleGroupRepository scheduleGroupRepository;

    @Autowired
    ScheduleDetailsRepository scheduleDetailsRepository;

    @Autowired
    DetailPlanRepository detailPlanRepository;

    @Autowired
    MemoRepository memoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MemoService memoService;

    final LocalDate START_DATE = LocalDate.of(2024, 9, 1);
    final LocalDate END_DATE = LocalDate.of(2024, 9, 5);

    @AfterEach
    void tearDown() {
        scheduleGroupRepository.deleteAllInBatch();
        memoRepository.deleteAllInBatch();
        detailPlanRepository.deleteAllInBatch();
        ;
        scheduleDetailsRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자가 세부 계획에서 메모를 작성한다.")
    void writeMemo() throws Exception {
        // given
        User user = getUser("user@gmail.com", "user");
        userRepository.save(user);

        Schedule schedule = getSchedule(START_DATE, END_DATE);
        scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = getScheduleGroup(schedule, user);
        scheduleGroupRepository.save(scheduleGroup);

        List<ScheduleDetails> scheduleDetailsList = getDatesBetween(START_DATE, END_DATE)
                .stream().map(date -> ScheduleDetails.create(date, schedule))
                .toList();
        scheduleDetailsRepository.saveAll(scheduleDetailsList);

        DetailedPlan detailedPlan = getDetailedPlan(scheduleDetailsList.get(0));
        detailPlanRepository.save(detailedPlan);

        MemoWriteServiceRequest request = MemoWriteServiceRequest.builder()
                .content("여행에 대한 메모")
                .build();
        // when
        memoService.writeMemo(user.getId(), detailedPlan.getId(), request);

        // then
        List<Memo> memos = memoRepository.findAll();
        assertThat(memos.get(0))
                .extracting("id", "content")
                .contains(memos.get(0).getId(), "여행에 대한 메모");
    }

    @Test
    @DisplayName("세부 계획이 없을 때는 메모를 작성하면 예외가 발생한다.")
    void validateDetailedPlan() throws Exception {
        // given
        User user = getUser("user@gmail.com", "user");
        userRepository.save(user);

        Schedule schedule = getSchedule(START_DATE, END_DATE);
        scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = getScheduleGroup(schedule, user);
        scheduleGroupRepository.save(scheduleGroup);

        List<ScheduleDetails> scheduleDetailsList = getDatesBetween(START_DATE, END_DATE)
                .stream().map(date -> ScheduleDetails.create(date, schedule))
                .toList();
        scheduleDetailsRepository.saveAll(scheduleDetailsList);

        MemoWriteServiceRequest request = MemoWriteServiceRequest.builder()
                .content("여행에 대한 메모")
                .build();

        // when // then
        assertThatThrownBy(() -> memoService.writeMemo(user.getId(), 100L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("세부 계획을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("일정 관리 그룹에 포함되어 있지 않은 유저가 세부 계획에 메모를 남기면 예외가 발샏한다.")
    void validateWriteMemo() throws Exception {
        // given
        User userA = getUser("userA@gmail.com", "userA");
        userRepository.save(userA);

        User userB = getUser("userB@gmail.com", "userB");
        userRepository.save(userB);

        Schedule schedule = getSchedule(START_DATE, END_DATE);
        scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = getScheduleGroup(schedule, userA);
        scheduleGroupRepository.save(scheduleGroup);

        List<ScheduleDetails> scheduleDetailsList = getDatesBetween(START_DATE, END_DATE)
                .stream().map(date -> ScheduleDetails.create(date, schedule))
                .toList();
        scheduleDetailsRepository.saveAll(scheduleDetailsList);

        DetailedPlan detailedPlan = getDetailedPlan(scheduleDetailsList.get(0));
        detailPlanRepository.save(detailedPlan);

        MemoWriteServiceRequest request = MemoWriteServiceRequest.builder()
                .content("여행에 대한 메모")
                .build();
        // when // then
        assertThatThrownBy(() -> memoService.writeMemo(userB.getId(), detailedPlan.getId(), request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("권한이 없는 사용자입니다.");
    }

    @Test
    @DisplayName("사용자가 세부 계획에서 메모를 수정한다.")
    void editMemo() throws Exception {
        // given
        User user = getUser("user@gmail.com", "user");
        userRepository.save(user);

        Schedule schedule = getSchedule(START_DATE, END_DATE);
        scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = getScheduleGroup(schedule, user);
        scheduleGroupRepository.save(scheduleGroup);

        List<ScheduleDetails> scheduleDetailsList = getDatesBetween(START_DATE, END_DATE)
                .stream().map(date -> ScheduleDetails.create(date, schedule))
                .toList();
        scheduleDetailsRepository.saveAll(scheduleDetailsList);

        DetailedPlan detailedPlan = getDetailedPlan(scheduleDetailsList.get(0));
        detailPlanRepository.save(detailedPlan);

        Memo memo = Memo.builder()
                .content("메모")
                .detailedPlan(detailedPlan)
                .build();
        memoRepository.save(memo);

        MemoEditServiceRequest request = MemoEditServiceRequest.builder()
                .content("메모 수정")
                .build();
        // when
        memoService.editMemo(user.getId(), detailedPlan.getId(), memo.getId(), request);

        // then
        List<Memo> memos = memoRepository.findAll();
        assertThat(memos.get(0))
                .extracting("id", "content")
                .contains(memos.get(0).getId(), "메모 수정");
    }

    @Test
    @DisplayName("사용자가 세부 계획에서 메모를 삭제한다")
    void deleteMemo() throws Exception {
        // given
        User user = getUser("user@gmail.com", "user");
        userRepository.save(user);

        Schedule schedule = getSchedule(START_DATE, END_DATE);
        scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = getScheduleGroup(schedule, user);
        scheduleGroupRepository.save(scheduleGroup);

        List<ScheduleDetails> scheduleDetailsList = getDatesBetween(START_DATE, END_DATE)
                .stream().map(date -> ScheduleDetails.create(date, schedule))
                .toList();
        scheduleDetailsRepository.saveAll(scheduleDetailsList);

        DetailedPlan detailedPlan = getDetailedPlan(scheduleDetailsList.get(0));
        detailPlanRepository.save(detailedPlan);

        Memo memo = Memo.builder()
                .content("메모")
                .detailedPlan(detailedPlan)
                .build();
        memoRepository.save(memo);
        // when
        memoService.deleteMemo(user.getId(), detailedPlan.getId(), memo.getId());

        // then
        List<Memo> memos = memoRepository.findAll();
        assertThat(memos.size()).isEqualTo(0);
    }

    private DetailedPlan getDetailedPlan(ScheduleDetails scheduleDetails) {
        return DetailedPlan.builder()
                .scheduleDetails(scheduleDetails)
                .name("제주공항")
                .latitude(11.1)
                .longitude(11.1)
                .sequence(1L)
                .build();
    }

    private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> !date.isAfter(endDate), date -> date.plusDays(1))
                .toList();
    }

    private static ScheduleGroup getScheduleGroup(Schedule schedule, User user) {
        return ScheduleGroup.builder()
                .schedule(schedule)
                .user(user)
                .build();
    }

    private static Schedule getSchedule(LocalDate start, LocalDate end) {
        return Schedule.builder()
                .title("제주도 여행")
                .city("제주도")
                .startDate(start)
                .endDate(end)
                .totalTravelDays(ChronoUnit.DAYS.between(start, end) + 1)
                .build();
    }

    private User getUser(String email, String name) {
        return User.builder()
                .email(email)
                .name(name)
                .build();
    }

}