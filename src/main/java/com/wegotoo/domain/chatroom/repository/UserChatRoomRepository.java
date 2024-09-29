package com.wegotoo.domain.chatroom.repository;

import com.wegotoo.domain.chatroom.UserChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long>, UserChatRoomRepositoryCustom {

    @Query("SELECT ucr FROM UserChatRoom ucr "
            + "JOIN FETCH ucr.user "
            + "WHERE ucr.chatRoom.id = :chatRoomId")
    List<UserChatRoom> findByChatRoomIdWithUser(@Param("chatRoomId") Long chatRoomId);

}
