package com.wegotoo.api.accompany;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.api.accompany.request.AccompanyCreateRequest;
import com.wegotoo.api.accompany.request.AccompanyEditRequest;
import com.wegotoo.application.accompany.AccompanyService;
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
public class AccompanyController {

    private final AccompanyService accompanyService;

    @PostMapping("/v1/accompanies")
    public ApiResponse<Void> createAccompany(@Auth Long userId,
                                             @RequestBody @Valid AccompanyCreateRequest request) {
        accompanyService.createAccompany(userId, request.toService());
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

}
