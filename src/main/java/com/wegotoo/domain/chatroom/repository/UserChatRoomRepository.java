package com.wegotoo.domain.chatroom.repository;

import com.wegotoo.domain.chatroom.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long>, UserChatRoomRepositoryCustom {
}
