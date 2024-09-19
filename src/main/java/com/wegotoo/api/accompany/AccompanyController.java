package com.wegotoo.api.accompany;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.api.accompany.request.AccompanyCreateRequest;
import com.wegotoo.application.accompany.AccompanyService;
import com.wegotoo.infra.resolver.auth.Auth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AccompanyController {

    private final AccompanyService accompanyService;

    @PostMapping("/v1/accompanies")
    public ApiResponse<Void> createAccompany(@Auth Long userId, @RequestBody @Valid AccompanyCreateRequest request) {
        accompanyService.createAccompany(userId, request.toService());
        return ApiResponse.ok();
    }

}
