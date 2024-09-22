package com.wegotoo.infra.socket;

import static com.wegotoo.exception.ErrorCode.NOT_VALID_USER;

import com.wegotoo.exception.BusinessException;
import com.wegotoo.infra.security.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtSocketAuthorizationHandler implements ChannelInterceptor {

    private final JwtTokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (isCommandConnect(accessor)) {
            String bearerToken = accessor.getFirstNativeHeader("Authorization");

            if (isNull(bearerToken) || !isValid(bearerToken)) {
                throw new BusinessException(NOT_VALID_USER);
            }

            Authentication authentication = tokenProvider.getAuthentication(removeBearer(bearerToken));
            accessor.setUser(authentication);
        }

        return message;
    }

    private static boolean isCommandConnect(StompHeaderAccessor accessor) {
        return accessor.getCommand() == StompCommand.CONNECT;
    }

    private boolean isNull(String bearerToken) {
        return bearerToken == null;
    }

    private boolean isValid(String bearerToken) {
        return tokenProvider.isValid(bearerToken);
    }

    private String removeBearer(String bearerToken) {
        return bearerToken.replace("Bearer ", "");
    }

}
