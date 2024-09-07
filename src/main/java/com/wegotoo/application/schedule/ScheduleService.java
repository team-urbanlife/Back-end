package com.wegotoo.application.schedule;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.application.schedule.request.ScheduleCreateServiceRequest;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleGroup;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
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

    @Transactional
    public void createSchedule(Long userId, ScheduleCreateServiceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Schedule schedule = Schedule.create(request.getCity(), request.getStartDate(), request.getEndDate(), request.totalTravelDays());
        scheduleRepository.save(schedule);

        ScheduleGroup scheduleGroup = ScheduleGroup.create(user, schedule);
        scheduleGroupRepository.save(scheduleGroup);
    }

}
