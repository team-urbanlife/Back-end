package com.wegotoo.application.schedule;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.application.schedule.request.ScheduleCreateServiceRequest;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.ScheduleGroup;
import com.wegotoo.domain.schedule.repository.ScheduleDetailsRepository;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ScheduleGroupRepository scheduleGroupRepository;
    private final ScheduleDetailsRepository scheduleDetailsRepository;

    @Transactional
    public void createSchedule(Long userId, ScheduleCreateServiceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Schedule schedule = Schedule.create(request.getCity(), request.getStartDate(),
                request.getEndDate(), request.totalTravelDays());
        scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = ScheduleGroup.create(user, schedule);
        scheduleGroupRepository.save(scheduleGroup);

        List<ScheduleDetails> scheduleDetailsList = getDatesBetween(request.getStartDate(), request.getEndDate())
                .stream().map(date -> ScheduleDetails.create(date, schedule))
                .toList();
        scheduleDetailsRepository.saveAll(scheduleDetailsList);
    }

    private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> !date.isAfter(endDate), date -> date.plusDays(1))
                .toList();
    }

}
