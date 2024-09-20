package com.wegotoo.domain.accompany.repository;

import com.wegotoo.domain.accompany.repository.response.AccompanyFindAllQueryEntity;
import java.util.List;

public interface AccompanyRepositoryCustom {
    List<AccompanyFindAllQueryEntity> accompanyFindAll(Integer offset, Integer size);
}
