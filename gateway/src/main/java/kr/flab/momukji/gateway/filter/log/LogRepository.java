package kr.flab.momukji.gateway.filter.log;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LogRepository extends MongoRepository<Log, String> {

    @Query(value = "{$and: [{'uri':?0}, {'status':?1}, {'requestBody.storeId':?2}]}", count = true)
    long countOrderNumber(String uri, String status, Long storeId);
}
