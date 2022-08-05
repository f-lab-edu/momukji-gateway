package kr.flab.momukji.gateway.filter.log;

import java.time.Instant;

import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.ToString;

@Builder
public class Log {

    @Id
    public String id;
    public String method;
    public String uri;
    public String parameters;
    public String ip;
    public Instant requestedTimestamp;
    public Instant responsedTimestamp;
    public String status;
}
