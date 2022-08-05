package kr.flab.momukji.gateway.filter;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import kr.flab.momukji.gateway.filter.log.Log;
import kr.flab.momukji.gateway.filter.log.LogService;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    @Autowired
    private LogService logService;

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        GatewayFilter filter = new OrderedGatewayFilter((exchange,chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            
            String method = request.getMethodValue();
            String uri = request.getURI().toString();
            String parameters = request.getQueryParams().toString();
            String ip = request.getRemoteAddress().getHostString();
            Instant requestedTimestamp = Instant.now();
            
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Instant responsedTimestamp = Instant.now();
                String status = String.valueOf(response.getStatusCode().value());
                
                Log log = Log.builder()
                    .method(method)
                    .uri(uri)
                    .parameters(parameters)
                    .ip(ip)
                    .requestedTimestamp(requestedTimestamp)
                    .responsedTimestamp(responsedTimestamp)
                    .status(status)
                    .build();
                
                logService.save(log);
            }));
        }, Ordered.LOWEST_PRECEDENCE);
        
        return filter;
    }

    public static class Config {

    }
}