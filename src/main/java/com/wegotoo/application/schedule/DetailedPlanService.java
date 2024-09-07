package com.wegotoo.application.schedule;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.application.schedule.request.DetailedPlanCreateServiceRequest;
import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.repository.DetailPlanRepository;
import com.wegotoo.domain.schedule.repository.ScheduleDetailsRepository;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.schedule.repository.ScheduleRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class DetailedPlanService {

    private final ScheduleDetailsRepository scheduleDetailsRepository;
    private final DetailPlanRepository detailPlanRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ScheduleGroupRepository scheduleGroupRepository;

    @Transactional
    public void writeDetailedPlan(Long scheduleDetailsId, Long userId, DetailedPlanCreateServiceRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        ScheduleDetails scheduleDetails = scheduleDetailsRepository.findById(scheduleDetailsId)
                .orElseThrow(() -> new BusinessException(SCHEDULE_DETAIL_NOT_FOUND));

        Schedule schedule = scheduleDetails.getSchedule();

        scheduleGroupRepository.findByUserIdAndScheduleId(user.getId(), schedule.getId())
                .orElseThrow(() -> new BusinessException(UNAUTHORIZED_REQUEST));

        Long sequence = detailPlanRepository.dayIncludedPlanCount(scheduleDetails.getId(), request.getDate());

        DetailedPlan detailedPlan = DetailedPlan.create(request.getType(), request.getName(),
                request.getMemo(), request.getLatitude(), request.getLongitude(), sequence, scheduleDetails);

        detailPlanRepository.save(detailedPlan);
    }
}
