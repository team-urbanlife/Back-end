package com.wegotoo.domain.chat.repository;

import com.wegotoo.domain.chat.OnlineUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineUserRepository extends CrudRepository<OnlineUser, Long> {
}
