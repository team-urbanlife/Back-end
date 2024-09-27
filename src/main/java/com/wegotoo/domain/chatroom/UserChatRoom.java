package com.wegotoo.domain.chatroom;

import com.wegotoo.domain.accompany.Accompany;
import com.wegotoo.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_chat_room_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_chat_room_role")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accompany_id")
    private Accompany accompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder
    private UserChatRoom(Role role, User user, Accompany accompany, ChatRoom chatRoom) {
        this.role = role;
        this.user = user;
        this.accompany = accompany;
        this.chatRoom = chatRoom;
    }

    public static UserChatRoom ofAdmin(User user, ChatRoom chatRoom, Accompany accompany) {
        return UserChatRoom.builder()
                .role(Role.ADMIN)
                .user(user)
                .chatRoom(chatRoom)
                .accompany(accompany)
                .build();
    }

    public static UserChatRoom ofGuest(User user, ChatRoom chatRoom, Accompany accompany) {
        return UserChatRoom.builder()
                .role(Role.GUEST)
                .user(user)
                .chatRoom(chatRoom)
                .accompany(accompany)
                .build();
    }

}
