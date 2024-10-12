package com.wegotoo.api.schedule;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.api.schedule.request.ScheduleCreateRequest;
import com.wegotoo.api.schedule.request.ScheduleEditRequest;
import com.wegotoo.application.OffsetLimit;
import com.wegotoo.application.SliceResponse;
import com.wegotoo.application.schedule.ScheduleService;
import com.wegotoo.application.schedule.response.ScheduleFindAllResponse;
import com.wegotoo.application.schedule.response.ScheduleResponse;
import com.wegotoo.application.schedule.response.TravelPlanResponse;
import com.wegotoo.infra.resolver.auth.Auth;
import jakarta.validation.Valid;
import java.util.List;
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
    public ApiResponse<ScheduleResponse> createSchedule(@RequestBody @Valid ScheduleCreateRequest request,
                                                        @Auth Long userId) {
        return ApiResponse.ok(scheduleService.createSchedule(userId, request.toService()));
    }

    @GetMapping("/v1/schedules")
    public ApiResponse<SliceResponse<ScheduleFindAllResponse>> findAllSchedules(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "4") Integer size,
            @Auth Long userId
    ) {
        return ApiResponse.ok(scheduleService.findAllSchedules(userId, OffsetLimit.of(page, size)));
    }

    @PatchMapping("/v1/schedules/{scheduleId}")
    public ApiResponse<Void> editSchedule(@PathVariable("scheduleId") Long scheduleId,
                                          @RequestBody @Valid ScheduleEditRequest request,
                                          @Auth Long userId) {
        scheduleService.editSchedule(userId, scheduleId, request.toService());
        return ApiResponse.ok();
    }

    @DeleteMapping("/v1/schedules/{scheduleId}")
    public ApiResponse<Void> deleteSchedule(@PathVariable("scheduleId") Long scheduleId,
                                            @Auth Long userId) {
        scheduleService.deleteSchedule(userId, scheduleId);
        return ApiResponse.ok();
    }

}
