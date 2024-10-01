package com.wegotoo.domain.user;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Users {

    private Map<Long, User> userMap;

    @Builder
    private Users(Map<Long, User> userMap) {
        this.userMap = userMap;
    }

    public static Users of(List<User> users) {
        return Users.builder()
                .userMap(users.stream()
                        .collect(toMap(User::getId, Function.identity())))
                .build();
    }

    public User findById(Long userId) {
        return userMap.get(userId);
    }

}
