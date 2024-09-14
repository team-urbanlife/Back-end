package com.wegotoo.api.schedule;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.application.schedule.ScheduleDetailsService;
import com.wegotoo.application.schedule.response.TravelPlanResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ScheduleDetailsController {

    private final ScheduleDetailsService scheduleDetailsService;

    @GetMapping("/v1/schedules/{scheduleId}/schedule-details")
    public ApiResponse<List<TravelPlanResponse>> findTravelPlans(@PathVariable("scheduleId") Long scheduleId) {
        return ApiResponse.ok(scheduleDetailsService.findTravelPlans(0L, scheduleId));
    }
}
