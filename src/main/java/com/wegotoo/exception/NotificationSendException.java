package com.wegotoo.exception;

import java.io.IOException;
import lombok.Getter;

@Getter
public class NotificationSendException extends IOException {

    private final int code;

    public NotificationSendException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
}
