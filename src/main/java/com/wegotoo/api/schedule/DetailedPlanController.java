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

    @PostMapping("/v1/schedule-details/{scheduleDetailsId}/detailed-plans")
    public ApiResponse<Void> writeDetailedPlan(@PathVariable("scheduleDetailsId") Long scheduleDetailsId, @RequestBody @Valid DetailedPlanCreateRequest request) {
        detailedPlanService.writeDetailedPlan(scheduleDetailsId, 0L, request.toService());
        return ApiResponse.ok();
    }
}
