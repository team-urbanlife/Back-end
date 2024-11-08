package com.wegotoo.domain.user.repository;

import com.wegotoo.domain.user.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
