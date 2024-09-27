package com.wegotoo.domain.chatroom.repository;

import static com.wegotoo.domain.accompany.QAccompany.accompany;
import static com.wegotoo.domain.chatroom.QChatRoom.chatRoom;
import static com.wegotoo.domain.chatroom.QUserChatRoom.userChatRoom;
import static com.wegotoo.domain.user.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wegotoo.domain.chatroom.QUserChatRoom;
import com.wegotoo.domain.chatroom.QUserChatRoomQueryEntity;
import com.wegotoo.domain.chatroom.UserChatRoomQueryEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserChatRoomRepositoryImpl implements UserChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserChatRoomQueryEntity> findAllByUserId(Long userId) {
        QUserChatRoom otherUserChatRoom = new QUserChatRoom("otherUserChatRoom");

        return queryFactory.select(new QUserChatRoomQueryEntity(
                        chatRoom.id,
                        accompany.id,
                        otherUserChatRoom.user.profileImage
                ))
                .from(userChatRoom)
                .join(userChatRoom.chatRoom, chatRoom)
                .join(otherUserChatRoom).on(otherUserChatRoom.chatRoom.id.eq(chatRoom.id))
                .join(user).on(user.id.eq(otherUserChatRoom.user.id))
                .join(accompany).on(accompany.id.eq(otherUserChatRoom.accompany.id))
                .where(userChatRoom.user.id.eq(userId).and(user.id.ne(userId)))
                .fetch();
    }

}
