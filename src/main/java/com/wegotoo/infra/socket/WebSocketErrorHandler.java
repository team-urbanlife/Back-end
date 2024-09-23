package com.wegotoo.infra.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Configuration
@RequiredArgsConstructor
public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {

    private final ObjectMapper om;

    @Override
    @SneakyThrows
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof BusinessException causeEx) {
            StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
            accessor.setLeaveMutable(true);

            ErrorResponse response = ErrorResponse.of(causeEx.getCode(), causeEx.getMessage());

            return MessageBuilder.createMessage(om.writeValueAsBytes(response), accessor.getMessageHeaders());
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

}
