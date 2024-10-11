package com.wegotoo.api.user;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.application.user.UserService;
import com.wegotoo.application.user.response.UserResponse;
import com.wegotoo.infra.resolver.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/v1/users/me")
    public ApiResponse<UserResponse> findUser(@Auth Long userId) {
        return ApiResponse.ok(userService.findUser(userId));
    }

}
