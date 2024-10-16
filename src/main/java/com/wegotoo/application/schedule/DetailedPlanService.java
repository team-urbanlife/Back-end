package com.wegotoo.application.schedule;

import static com.wegotoo.exception.ErrorCode.CANNOT_MOVE_SEQUENCE;
import static com.wegotoo.exception.ErrorCode.DETAILED_PLAN_NOT_FOUND;
import static com.wegotoo.exception.ErrorCode.SCHEDULE_DETAIL_NOT_FOUND;
import static com.wegotoo.exception.ErrorCode.UNAUTHORIZED_REQUEST;
import static com.wegotoo.exception.ErrorCode.USER_NOT_FOUND;

import com.wegotoo.api.schedule.request.DetailedPlanMoveRequest;
import com.wegotoo.application.schedule.request.DetailedPlanCreateServiceRequest;
import com.wegotoo.application.schedule.request.DetailedPlanEditServiceRequest;
import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.repository.DetailPlanRepository;
import com.wegotoo.domain.schedule.repository.MemoRepository;
import com.wegotoo.domain.schedule.repository.ScheduleDetailsRepository;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class DetailedPlanService {

    private final ScheduleDetailsRepository scheduleDetailsRepository;
    private final DetailPlanRepository detailPlanRepository;
    private final UserRepository userRepository;
    private final ScheduleGroupRepository scheduleGroupRepository;
    private final MemoRepository memoRepository;

    @Transactional
    public void writeDetailedPlan(Long scheduleDetailsId, Long userId, DetailedPlanCreateServiceRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        ScheduleDetails scheduleDetails = scheduleDetailsRepository.findById(scheduleDetailsId)
                .orElseThrow(() -> new BusinessException(SCHEDULE_DETAIL_NOT_FOUND));

        Schedule schedule = scheduleDetails.getSchedule();

        validateUserHasAccessToSchedule(user.getId(), schedule.getId());

        Long sequence = detailPlanRepository.dayIncludedPlanCount(scheduleDetails.getId(), request.getDate());

        DetailedPlan detailedPlan = DetailedPlan.create(request.getName(),
                request.getLatitude(), request.getLongitude(), sequence, scheduleDetails);

        detailPlanRepository.save(detailedPlan);
    }

    @Transactional
    public void editDetailedPlan(Long detailedPlanId, Long userId, DetailedPlanEditServiceRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        DetailedPlan detailedPlan = detailPlanRepository.findById(detailedPlanId)
                .orElseThrow(() -> new BusinessException(DETAILED_PLAN_NOT_FOUND));

        ScheduleDetails scheduleDetails = detailedPlan.getScheduleDetails();
        Schedule schedule = scheduleDetails.getSchedule();

        validateUserHasAccessToSchedule(user.getId(), schedule.getId());

        detailedPlan.edit(request.getName(), request.getLatitude(), request.getLongitude());
    }

    @Transactional
    public void movePlan(Long scheduleDetailsId, Long userId, List<DetailedPlanMoveRequest> requests) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        ScheduleDetails scheduleDetails = scheduleDetailsRepository.findById(scheduleDetailsId)
                .orElseThrow(() -> new BusinessException(SCHEDULE_DETAIL_NOT_FOUND));

        Schedule schedule = scheduleDetails.getSchedule();
        validateUserHasAccessToSchedule(user.getId(), schedule.getId());

        requests.forEach(planMove -> {
            DetailedPlan detailedPlan = detailPlanRepository.findById(planMove.getDetailedPlanId())
                    .orElseThrow(() -> new BusinessException(DETAILED_PLAN_NOT_FOUND));
            detailedPlan.movePlan(planMove.getSequence());
        });
    }

    @Transactional
    public void deleteDetailedPlan(Long detailedPlanId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        DetailedPlan detailedPlan = detailPlanRepository.findById(detailedPlanId)
                .orElseThrow(() -> new BusinessException(DETAILED_PLAN_NOT_FOUND));

        validateUserHasAccessToSchedule(user.getId(), detailedPlan.getId());

        memoRepository.deleteByDetailedPlan(detailedPlan);
        detailPlanRepository.delete(detailedPlan);
    }

    private void validateUserHasAccessToSchedule(Long userId, Long scheduleId) {
        scheduleGroupRepository.findByUserIdAndScheduleId(userId, scheduleId)
                .orElseThrow(() -> new BusinessException(UNAUTHORIZED_REQUEST));
    }
}
