package com.wegotoo.domain.chatroom.repository;

import com.wegotoo.domain.chatroom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
