package com.wegotoo.domain.accompany.repository;

import com.wegotoo.application.accompany.response.AccompanyFindAllResponse;
import com.wegotoo.domain.accompany.repository.response.AccompanyFindAllQueryEntity;
import com.wegotoo.domain.accompany.repository.response.AccompanyFindOneQueryEntity;
import java.util.List;

public interface AccompanyRepositoryCustom {
    List<AccompanyFindAllQueryEntity> accompanyFindAll(Integer offset, Integer size);

    List<AccompanyFindAllQueryEntity> findAllAccompanyByUserId(Long userid, Integer offset, Integer limit);

    AccompanyFindOneQueryEntity accompanyFindOne(Long accompanyId);
}
