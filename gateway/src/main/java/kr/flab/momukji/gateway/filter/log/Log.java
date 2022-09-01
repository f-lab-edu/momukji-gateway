package kr.flab.momukji.gateway.filter.log;

import java.time.Instant;

import org.springframework.data.annotation.Id;

import com.mongodb.DBObject;

import lombok.Builder;

@Builder
public class Log {

    @Id
    public String id;
    public String method;
    public String uri;
    public DBObject requestParams;
    public DBObject requestBody;
    public String ip;
    public Instant requestedTimestamp;
    public Instant responsedTimestamp;
    public String status;
}
