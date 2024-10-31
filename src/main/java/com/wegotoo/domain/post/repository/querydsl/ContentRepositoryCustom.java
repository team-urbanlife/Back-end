package com.wegotoo.domain.post.repository.querydsl;

import com.wegotoo.domain.post.repository.response.ContentQueryEntity;
import java.util.List;
import java.util.Map;

public interface ContentRepositoryCustom {

    Map<Long, List<ContentQueryEntity>> findAllByPostIds(List<Long> postIds);

}
