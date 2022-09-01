package kr.flab.momukji.gateway.filter.log;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LogRepository extends MongoRepository<Log, String> {

    @Query(value = "{$and: [{'uri':'/api/order'}, {'status':'200'}, {'requestBody.storeId':?0}]}", count = true)
    long countOrders(Long storeId);
}
