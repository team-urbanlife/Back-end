package com.wegotoo.domain.chatroom.repository;

import com.wegotoo.domain.chatroom.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c " +
            "JOIN UserChatRoom ucr ON ucr.chatRoom = c " +
            "WHERE ucr.user.id = :userId AND ucr.accompany.id = :accompanyId")
    Optional<ChatRoom> findByUserIdAndAccompanyId(@Param("userId") Long userId, @Param("accompanyId") Long accompanyId);

}
