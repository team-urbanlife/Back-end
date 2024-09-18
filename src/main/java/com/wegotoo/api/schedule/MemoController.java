package com.wegotoo.api.schedule;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.api.schedule.request.MemoEditRequest;
import com.wegotoo.application.schedule.MemoService;
import com.wegotoo.application.schedule.request.MemoWriteRequest;
import com.wegotoo.infra.resolver.auth.Auth;
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
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/v1/detailed-plans/{detailedPlanId}/memos")
    public ApiResponse<Void> writeMemo(@PathVariable("detailedPlanId") Long detailedPlanId,
                                       @RequestBody @Valid MemoWriteRequest request,
                                       @Auth Long userId) {
        memoService.writeMemo(userId, detailedPlanId, request.toService());
        return ApiResponse.ok();
    }

    @PatchMapping("/v1/detailed-plans/{detailedPlanId}/memos/{memoId}")
    public ApiResponse<Void> editMemo(@PathVariable("detailedPlanId") Long detailedPlanId,
                                      @PathVariable("memoId") Long memoId,
                                      @RequestBody @Valid MemoEditRequest request,
                                      @Auth Long userId) {
        memoService.editMemo(userId, detailedPlanId, memoId, request.toService());
        return ApiResponse.ok();
    }

    @DeleteMapping("/v1/detailed-plans/{detailedPlanId}/memos/{memoId}")
    public ApiResponse<Void> deleteMemo(@PathVariable("detailedPlanId") Long detailedPlanId,
                                        @PathVariable("memoId") Long memoId,
                                        @Auth Long userId) {
        memoService.deleteMemo(userId, detailedPlanId, memoId);
        return ApiResponse.ok();
    }
}
