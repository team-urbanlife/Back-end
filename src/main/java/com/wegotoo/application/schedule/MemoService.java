package com.wegotoo.application.schedule;

import static com.wegotoo.exception.ErrorCode.*;

import com.wegotoo.api.schedule.request.MemoEditServiceRequest;
import com.wegotoo.application.schedule.request.MemoWriteServiceRequest;
import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.Memo;
import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.repository.DetailPlanRepository;
import com.wegotoo.domain.schedule.repository.MemoRepository;
import com.wegotoo.domain.schedule.repository.ScheduleGroupRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final DetailPlanRepository detailPlanRepository;
    private final UserRepository userRepository;
    private final ScheduleGroupRepository scheduleGroupRepository;

    @Transactional
    public void writeMemo(Long userId, Long detailedPlanId, MemoWriteServiceRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        DetailedPlan detailedPlan = detailPlanRepository.findById(detailedPlanId)
                .orElseThrow(() -> new BusinessException(DETAILED_PLAN_NOT_FOUND));

        ScheduleDetails scheduleDetails = detailedPlan.getScheduleDetails();
        Schedule schedule = scheduleDetails.getSchedule();

        validateUserHasAccessToSchedule(user.getId(), schedule.getId());

        Memo memo = Memo.write(request.getContent(), detailedPlan);
        memoRepository.save(memo);
    }

    @Transactional
    public void editMemo(Long userId, Long detailedPlanId, Long memoId, MemoEditServiceRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        DetailedPlan detailedPlan = detailPlanRepository.findById(detailedPlanId)
                .orElseThrow(() -> new BusinessException(DETAILED_PLAN_NOT_FOUND));

        ScheduleDetails scheduleDetails = detailedPlan.getScheduleDetails();
        Schedule schedule = scheduleDetails.getSchedule();

        validateUserHasAccessToSchedule(user.getId(), schedule.getId());

        Memo memo = memoRepository.findById(memoId).orElseThrow(() -> new BusinessException(MEMO_NOT_FOUND));

        memo.edit(request.getContent());
    }

    @Transactional
    public void deleteMemo(Long userId, Long detailedPlanId, Long memoId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        DetailedPlan detailedPlan = detailPlanRepository.findById(detailedPlanId)
                .orElseThrow(() -> new BusinessException(DETAILED_PLAN_NOT_FOUND));

        ScheduleDetails scheduleDetails = detailedPlan.getScheduleDetails();
        Schedule schedule = scheduleDetails.getSchedule();

        validateUserHasAccessToSchedule(user.getId(), schedule.getId());

        Memo memo = memoRepository.findById(memoId).orElseThrow(() -> new BusinessException(MEMO_NOT_FOUND));

        memoRepository.delete(memo);
    }

    private void validateUserHasAccessToSchedule(Long userId, Long scheduleId) {
        scheduleGroupRepository.findByUserIdAndScheduleId(userId, scheduleId)
                .orElseThrow(() -> new BusinessException(UNAUTHORIZED_REQUEST));
    }

}

