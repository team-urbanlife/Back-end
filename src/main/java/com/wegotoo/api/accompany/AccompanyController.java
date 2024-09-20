package com.wegotoo.api.accompany;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.api.accompany.request.AccompanyCreateRequest;
import com.wegotoo.api.accompany.request.AccompanyEditRequest;
import com.wegotoo.application.OffsetLimit;
import com.wegotoo.application.SliceResponse;
import com.wegotoo.application.accompany.AccompanyService;
import com.wegotoo.application.accompany.response.AccompanyFindAllResponse;
import com.wegotoo.application.accompany.response.AccompanyFindOneResponse;
import com.wegotoo.infra.resolver.auth.Auth;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
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
public class AccompanyController {

    private final AccompanyService accompanyService;

    @PostMapping("/v1/accompanies")
    public ApiResponse<Void> createAccompany(@Auth Long userId,
                                             @RequestBody @Valid AccompanyCreateRequest request) {
        LocalDateTime date = LocalDateTime.now();
        accompanyService.createAccompany(userId, request.toService(), date);
        return ApiResponse.ok();
    }

    @PatchMapping("/v1/accompanies/{accompanyId}")
    public ApiResponse<Void> editAccompany(@Auth Long userId,
                                           @PathVariable("accompanyId") Long accompanyId,
                                           @RequestBody @Valid AccompanyEditRequest request) {
        accompanyService.editAccompany(userId, accompanyId, request.toService());
        return ApiResponse.ok();
    }

    @DeleteMapping("/v1/accompanies/{accompanyId}")
    public ApiResponse<Void> deleteAccompany(@Auth Long userId,
                                             @PathVariable("accompanyId") Long accompanyId) {
        accompanyService.deleteAccompany(userId, accompanyId);
        return ApiResponse.ok();
    }

    @GetMapping("/v1/accompanies")
    public ApiResponse<SliceResponse<AccompanyFindAllResponse>> findAllAccompany(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "4") Integer size
    ) {
        return ApiResponse.ok(accompanyService.findAllAccompany(OffsetLimit.of(page, size)));
    }

    @GetMapping("/v1/accompanies/{accompanyId}")
    public ApiResponse<AccompanyFindOneResponse> findOneAccompany(@PathVariable("accompanyId") Long accompanyId) {
        return ApiResponse.ok(accompanyService.findOneAccompany(accompanyId));
    }

}
