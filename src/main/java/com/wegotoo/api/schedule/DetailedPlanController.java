package com.wegotoo.api.schedule;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.api.schedule.request.DetailedPlanCreateRequest;
import com.wegotoo.application.schedule.DetailedPlanService;
import com.wegotoo.application.schedule.request.DetailedPlanEditRequest;
import com.wegotoo.application.schedule.request.MovePlanRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DetailedPlanController {

    private final DetailedPlanService detailedPlanService;

    @PostMapping("/v1/schedule-details/{scheduleDetailsId}/detailed-plans")
    public ApiResponse<Void> writeDetailedPlan(@PathVariable("scheduleDetailsId") Long scheduleDetailsId,
                                               @RequestBody @Valid DetailedPlanCreateRequest request) {
        detailedPlanService.writeDetailedPlan(scheduleDetailsId, 0L, request.toService());
        return ApiResponse.ok();
    }

    @PatchMapping("/v1/detailed-plans/{detailedPlanId}")
    public ApiResponse<Void> editDetailedPlan(@PathVariable("detailedPlanId") Long detailedPlanId,
                                              @RequestBody @Valid DetailedPlanEditRequest request) {
        detailedPlanService.editDetailedPlan(detailedPlanId, 0L, request.toService());
        return ApiResponse.ok();
    }

    @PatchMapping("/v1/detailed-plans/{detailedPlanId}/move")
    public ApiResponse<Void> movePlan(@PathVariable("detailedPlanId") Long detailedPlanId,
                                      @RequestBody MovePlanRequest request) {
        detailedPlanService.movePlan(detailedPlanId, 0L, request.isMoveUp());
        return ApiResponse.ok();
    }

    @DeleteMapping("/v1/detailed-plans/{detailedPlanId}")
    public ApiResponse<Void> deleteDetailedPlan(@PathVariable("detailedPlanId") Long detailedPlanId) {
        detailedPlanService.deleteDetailedPlan(detailedPlanId, 0L);
        return ApiResponse.ok();
    }

}
