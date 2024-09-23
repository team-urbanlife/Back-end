package com.wegotoo.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private int code;
    private String message;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of(int code, String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse of(BusinessException ex) {
        return new ErrorResponse(ex.getCode(), ex.getMessage());
    }

}
