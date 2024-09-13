package com.wegotoo.api.schedule;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.api.schedule.request.ScheduleCreateRequest;
import com.wegotoo.application.OffsetLimit;
import com.wegotoo.application.SliceResponse;
import com.wegotoo.application.schedule.ScheduleService;
import com.wegotoo.api.schedule.request.ScheduleEditRequest;
import com.wegotoo.application.schedule.response.ScheduleFindAllResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/v1/schedules")
    public ApiResponse<Void> createSchedule(@RequestBody @Valid ScheduleCreateRequest request) {
        //todo 로그인 기능 구현될 시 0L 대신 로그인 유저 정보 값으로 변경 예정
        scheduleService.createSchedule(0L, request.toService());
        return ApiResponse.ok();
    }

    @GetMapping("/v1/schedules")
    public ApiResponse<SliceResponse<ScheduleFindAllResponse>> findAllSchedules(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "4") Integer size
    ) {
        return ApiResponse.ok(scheduleService.findAllSchedules(0L, OffsetLimit.of(page, size)));
    }

    @PatchMapping("/v1/schedules/{scheduleId}")
    public ApiResponse<Void> editSchedule(@PathVariable("scheduleId") Long scheduleId,
                                          @RequestBody @Valid ScheduleEditRequest request) {
        scheduleService.editSchedule(0L, scheduleId, request.toService());
        return ApiResponse.ok();
    }

    @DeleteMapping("/v1/schedules/{scheduleId}")
    public ApiResponse<Void> deleteSchedule(@PathVariable("scheduleId") Long scheduleId) {
        scheduleService.deleteSchedule(0L, scheduleId);
        return ApiResponse.ok();
    }

}
