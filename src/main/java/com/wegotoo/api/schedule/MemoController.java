package com.wegotoo.api.schedule;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.application.schedule.MemoService;
import com.wegotoo.application.schedule.request.MemoWriteRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
                                       @RequestBody @Valid MemoWriteRequest request) {
        // todo. 사용자 인증 로직 추가 되면 변경 예정
        memoService.writeMemo(0L, detailedPlanId, request.toService());
        return ApiResponse.ok();
    }
}
