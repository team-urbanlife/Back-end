package com.wegotoo.infra.socket;

import static com.wegotoo.infra.socket.PayloadType.ENTER_USER;
import static com.wegotoo.infra.socket.PayloadType.LEAVE_USER;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WebSocketResponse<T> {

    private PayloadType payloadType;
    private T payload;

    @Builder
    private WebSocketResponse(PayloadType payloadType, T payload) {
        this.payloadType = payloadType;
        this.payload = payload;
    }

    public static <T> WebSocketResponse<T> ofEnterUser(T payload) {
        return new WebSocketResponse<>(ENTER_USER, payload);
    }

    public static <T> WebSocketResponse<T> ofLeaveUser(T payload) {
        return new WebSocketResponse<>(LEAVE_USER, payload);
    }

}
