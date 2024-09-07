package com.wegotoo.api.schedule;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.api.schedule.request.DetailedPlanCreateRequest;
import com.wegotoo.application.schedule.DetailedPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DetailedPlanController {

    private final DetailedPlanService detailedPlanService;

    @PostMapping("/v1/schedules/{scheduleId}/detailplans")
    public ApiResponse<Void> createDetailPlan(@PathVariable("scheduleId") Long scheduleId, @RequestBody @Valid DetailedPlanCreateRequest request) {
        detailedPlanService.createDetailPlan(scheduleId, 0L, request.toService());
        return ApiResponse.ok();
    }
}
