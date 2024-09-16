package com.wegotoo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(404, "일정을 찾을 수 없습니다."),
    UNAUTHORIZED_REQUEST(400, "권한이 없는 사용자입니다."),
    INVALID_PARAMS_VALUE(400, "유효하지 않은 파라미터 입니다."),
    SCHEDULE_DETAIL_NOT_FOUND(404, "세부 일정을 찾을 수 없습니다."),
    DETAILED_PLAN_NOT_FOUND(404, "세부 계획을 찾을 수 없습니다."),
    CANNOT_MOVE_SEQUENCE(500, "순서를 이동할 수 없습니다."),
    INVALID_REFRESH_TOKEN(401, "유효하지 않은 리프레시 토큰입니다."),
    NOT_FOUND_REFRESH_TOKEN(404, "리프레시 토큰을 찾을 수 없습니다."),
    NOT_VALID_USER(401, "인증되지 않은 사용자입니다."),
    NOT_FOUND_ACCOMPANY(404, "동행 게시글을 찾을 수 없습니다.");

    private final int code;
    private final String message;

    ErrorCode(HttpStatus code, String message) {
        this.code = code.value();
        this.message = message;
    }

}
