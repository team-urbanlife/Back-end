package com.wegotoo.domain.chat.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.replaceRoot;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import com.wegotoo.domain.chat.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Chat> findAllByChatRoomId(Long chatRoomId, String cursorId, Integer limit) {
        Query query = new Query();

        query.addCriteria(Criteria.where("chatRoomId").is(chatRoomId));

        if (cursorId != null) {
            query.addCriteria(Criteria.where("_id").lt(cursorId));
        }

        query.limit(limit + 1);
        query.with(Sort.by(Sort.Direction.DESC, "_id"));

        return mongoTemplate.find(query, Chat.class);
    }

    @Override
    public List<Chat> findLatestChatsByChatRoomIds(List<Long> chatRoomIds) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("chatRoomId").in(chatRoomIds)),
                sort(Direction.DESC , "_id"),
                group("chatRoomId")
                        .first("$$ROOT").as("latestMessage"),
                replaceRoot("latestMessage"),
                sort(Direction.DESC, "_id")
        );

        AggregationResults<Chat> results = mongoTemplate.aggregate(aggregation, "chat", Chat.class);
        return results.getMappedResults();
    }

}
